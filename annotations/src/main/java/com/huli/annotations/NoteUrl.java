package com.huli.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface NoteUrl {

    String description() default "";

    String method();

    String[] requestHeaders() default {};

    String[] responseHeaders() default {};

    String[] pages() default {};

    String[] parameters() default {};

    boolean needLogin() default true;

    boolean webView() default false;

    String response() default "";

    boolean test();
}
