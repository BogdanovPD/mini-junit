package com.pavel.mini.junit.processors;

import java.util.List;

public interface AnnotationProcessor {

    void process(List<Class<?>> classes);

}
