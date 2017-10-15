package com.huli.compiler.entity;

import com.huli.compiler.Parameter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by su on 17-4-14.
 */

public class NoteUrlEntity {
    private String[] pages;
    private String description;
    private String url;
    private String method;
    private String[] requestHeaders;
    private String[] responseHeaders;
    private ArrayList<Parameter> parameters = new ArrayList<>();
    private boolean needLogin;
    private boolean webView;
    private String response;
    private boolean test;

    public String[] getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String[] requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String[] getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public boolean isWebView() {
        return webView;
    }

    public void setWebView(boolean webView) {
        this.webView = webView;
    }

    public String[] getPages() {
        return pages;
    }

    public void setPages(String[] pages) {
        this.pages = pages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "NoteUrlEntity{" +
                "pages=" + Arrays.toString(pages) +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", requestHeaders=" + Arrays.toString(requestHeaders) +
                ", responseHeaders=" + Arrays.toString(responseHeaders) +
                ", parameters=" + parameters +
                ", needLogin=" + needLogin +
                ", webView=" + webView +
                ", response='" + response + '\'' +
                ", test=" + test +
                '}';
    }
}
