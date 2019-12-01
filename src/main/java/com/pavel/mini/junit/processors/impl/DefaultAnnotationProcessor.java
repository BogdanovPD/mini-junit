package com.pavel.mini.junit.processors.impl;

import com.pavel.mini.junit.annotations.Test;
import com.pavel.mini.junit.processors.AnnotationProcessor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class DefaultAnnotationProcessor implements AnnotationProcessor {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void process(List<Class<?>> classes) {
        classes.forEach(clazz -> Stream.of(clazz.getMethods()).forEach(method -> processTest(clazz, method)));
    }

    private void processTest(Class<?> clazz, Method method) {
        if (method.getAnnotation(Test.class) == null) {
            return;
        }
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            method.invoke(instance);
        } catch (Exception e) {
            String msg = "TEST FAILED: %s\n---------------------------\nFULL CAUSE EXCEPTION:\n%s";
            String localizedMessage = e.getLocalizedMessage();
            System.out.println(ANSI_RED + String.format(msg, localizedMessage == null ? "No further information message"
                    : localizedMessage, e) + ANSI_RESET);
            return;
        }
        System.out.println(ANSI_GREEN + "TEST PASSED!" + ANSI_RESET);
    }

}
