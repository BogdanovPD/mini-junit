package com.pavel.mini.junit;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {
        MiniJunitCore.runClasses(List.of(TestCase.class));
    }

}
