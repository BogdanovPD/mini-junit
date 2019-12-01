package com.pavel.mini.junit;

import com.pavel.mini.junit.annotations.Processor;
import com.pavel.mini.junit.processors.AnnotationProcessor;
import com.pavel.mini.junit.processors.impl.DefaultAnnotationProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Slf4j
public class MiniJunitCore {

    public static void main(String... args) {
        List<Class<?>> classes = parseOptions(args);
        runClasses(classes);
    }

    public static void runClasses(List<Class<?>> classes) {
        Map<AnnotationProcessor, List<Class<?>>> processorListMap =
                classes.stream().collect(groupingBy(MiniJunitCore::getAnnotationProcessor));
        processorListMap.forEach(AnnotationProcessor::process);
    }

    private static List<Class<?>> parseOptions(String... args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        Option testCaseOption = new Option("t", "tests", true, "Test cases (separated by space)");
        testCaseOption.setArgs(Option.UNLIMITED_VALUES);
        testCaseOption.setRequired(true);
        options.addOption(testCaseOption);

        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp("MiniJunit", options);
        }

        if (!cmd.hasOption("t")) {
            throw new IllegalArgumentException("Please provide test cases");
        }

        String optionValue = cmd.getOptionValue("t");
        if (!optionValue.trim().contains("\\s")) {
            System.out.println("no spaces");
            try {
                return List.of(Class.forName(optionValue));
            } catch (ClassNotFoundException e) {
                log.error(e.getLocalizedMessage());
                e.printStackTrace();
                return Collections.emptyList();
            }
        }
        String[] classes = optionValue.split("\\s++");
        System.out.println(classes);

        var classesList = new ArrayList<Class<?>>();
        for (int i = 0; i < classes.length; i++) {
            try {
                classesList.add(Class.forName(classes[i]));
            } catch (ClassNotFoundException e) {
                log.error(e.getLocalizedMessage());
                e.printStackTrace();
                return Collections.emptyList();
            }
        }

        return classesList;

    }

    private static AnnotationProcessor getAnnotationProcessor(Class<?> clazz) {
        Processor processor = clazz.getAnnotation(Processor.class);
        if (processor != null) {
            Class<? extends AnnotationProcessor> processorClass = processor.value();
            try {
                return processorClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException e) {
                log.error("Cannot instantiate AnnotationProcessor class:\n".concat(e.getLocalizedMessage()));
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException AnnotationProcessor class:\n".concat(e.getLocalizedMessage()));
            } catch (InvocationTargetException e) {
                log.error("InvocationTargetException AnnotationProcessor class:\n".concat(e.getLocalizedMessage()));
            } catch (NoSuchMethodException e) {
                log.error("Cannot find non args constructor for AnnotationProcessor class:\n".concat(e.getLocalizedMessage()));
            }
        }
        return new DefaultAnnotationProcessor();

    }

}
