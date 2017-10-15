package com.huli.compiler;

import com.alibaba.fastjson.JSON;
import com.google.auto.service.AutoService;
import com.huli.annotations.NoteComponent;
import com.huli.annotations.NoteJsCallAndroid;
import com.huli.annotations.NoteUrl;
import com.huli.compiler.entity.NoteComponentEntity;
import com.huli.compiler.entity.NoteJsCallAndroidEntity;
import com.huli.compiler.entity.NoteUrlEntity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class NoteProcessor extends AbstractProcessor {

    private static final String WORKING_DIR = System.getProperty("user.dir");
    private Messager mMessager;

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager = processingEnv.getMessager();
        if (!roundEnvironment.processingOver()) {
            note(mMessager, "start processing...");
            mMessager.printMessage(Diagnostic.Kind.NOTE, "WORKING_DIR: " + WORKING_DIR);
            processNoteJsCallAndroid(roundEnvironment);
            processNoteUrl(roundEnvironment);
            processNoteComponent(roundEnvironment);
        } else {
            note(mMessager, "processed!");
        }
        return true;
    }

    private void processNoteJsCallAndroid(RoundEnvironment roundEnvironment) {
        Map<String, List<NoteJsCallAndroidEntity>> allResults = new HashMap<>();
        Collection<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(NoteJsCallAndroid.class);
        for (Element e : annotatedElements) {
            String className = e.getEnclosingElement().getSimpleName().toString();
            List<NoteJsCallAndroidEntity> results = allResults.get(className);
            if (results == null) {
                results = new ArrayList<>();
                allResults.put(className, results);
            }

            NoteJsCallAndroid noteJsCallAndroid = e.getAnnotation(NoteJsCallAndroid.class);
            NoteJsCallAndroidEntity entity = new NoteJsCallAndroidEntity();
            entity.setDescription(noteJsCallAndroid.description());
            entity.setParameters(noteJsCallAndroid.parameters());
            entity.setFunctionName(e.getSimpleName().toString());

            results.add(entity);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "entity: " + entity);
        }

        Set<String> keySet = allResults.keySet();
        for (String key : keySet) {
            List<NoteJsCallAndroidEntity> results = allResults.get(key);
            String jsonString = JSON.toJSONString(results, true);
            mMessager.printMessage(Diagnostic.Kind.NOTE, key + ": " + jsonString);
            save(key, jsonString);
        }
    }

    private void processNoteUrl(RoundEnvironment roundEnvironment) {
        List<NoteUrlEntity> list = new ArrayList<>();
        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(NoteUrl.class);
        Set<VariableElement> fields = ElementFilter.fieldsIn(annotatedElements);
        for (VariableElement e : fields) {
            NoteUrl noteUrl = e.getAnnotation(NoteUrl.class);
            NoteUrlEntity entity = new NoteUrlEntity();
            entity.setPages(noteUrl.pages());
            entity.setDescription(noteUrl.description());
            entity.setMethod(noteUrl.method());
            entity.setRequestHeaders(noteUrl.requestHeaders());
            entity.setResponseHeaders(noteUrl.responseHeaders());
            entity.setParameters(makeParameters(noteUrl.parameters()));
            entity.setUrl(e.getConstantValue().toString());
            entity.setWebView(noteUrl.webView());
            entity.setNeedLogin(noteUrl.needLogin());
            entity.setResponse(noteUrl.response());
            entity.setTest(noteUrl.test());
            list.add(entity);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "entity: " + entity);
        }

        String jsonString = JSON.toJSONString(list, true);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "url: " + jsonString);
        save("url", jsonString);
    }

    private void processNoteComponent(RoundEnvironment roundEnvironment) {
        List<NoteComponentEntity> list = new ArrayList<>();
        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(NoteComponent.class);
        Set<TypeElement> fields = ElementFilter.typesIn(annotatedElements);
        for (TypeElement e : fields) {
            NoteComponent noteComponent = e.getAnnotation(NoteComponent.class);
            NoteComponentEntity entity = new NoteComponentEntity();
            entity.setDescription(noteComponent.description());
            entity.setClassName(e.getQualifiedName().toString());
            list.add(entity);
            mMessager.printMessage(Diagnostic.Kind.NOTE, "entity: " + entity);
        }

        String jsonString = JSON.toJSONString(list, true);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "activities: " + jsonString);
        save("activities", jsonString);
    }

    private ArrayList<Parameter> makeParameters(String[] parameters) {
        ArrayList<Parameter> list = new ArrayList<>();
        if (parameters != null) {
            for (String parameter : parameters) {
                int index = parameter.indexOf("=");
                list.add(new Parameter(parameter.substring(0, index), parameter.substring(index + 1, parameter.length())));
            }
        }
        return list;
    }

    private void save(String filename, String result) {
        try {
            List<String> lines = Arrays.asList(result);
            Path file = Paths.get(WORKING_DIR + "/app/src/debug/assets/generated/" + filename + ".json");
            mMessager.printMessage(Diagnostic.Kind.NOTE, "PATH: " + file.toAbsolutePath());
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void note(Messager messager, CharSequence note) {
        messager.printMessage(Diagnostic.Kind.NOTE, note);
    }

    private static void error(Messager messager, CharSequence error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(NoteJsCallAndroid.class);
        annotations.add(NoteUrl.class);
        return annotations;
    }
}
