package com.pluralsight.courseinfo.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void id() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Course course = new Course("", "Test", 1, "Test");
        });

        assertEquals("No value present!", exception.getMessage());
    }

    @Test
    void name() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Course course = new Course("Test", null, 1, "Test");
        });

        assertEquals("No value present!", exception.getMessage());
    }

    @Test
    void url() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Course course = new Course("Test", "Test", 1, "");
        });

        assertEquals("No value present!", exception.getMessage());
    }
}