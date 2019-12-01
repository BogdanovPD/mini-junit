package com.pavel.mini.junit.annotations;

import com.pavel.mini.junit.processors.AnnotationProcessor;
import com.pavel.mini.junit.processors.impl.DefaultAnnotationProcessor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Processor {

    Class<? extends AnnotationProcessor> value() default DefaultAnnotationProcessor.class;

}
