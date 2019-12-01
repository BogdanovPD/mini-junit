package com.pavel.mini.junit;

import com.pavel.mini.junit.annotations.Test;

public class TestCase {

    @Test
    public void testMethod_passed() {

    }

    @Test
    public void testMethod_failed() {
        throw new RuntimeException("Exception msg");
    }

}
