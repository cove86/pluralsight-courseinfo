package com.pluralsight.courseinfo.domain;

import org.junit.jupiter.api.Test;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void id() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Course course = new Course("", "Test", 1, "Test", Optional.empty());
        });

        assertEquals("No value present!", exception.getMessage());
    }

    @Test
    void name() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Course course = new Course("Test", null, 1, "Test", Optional.empty());
        });

        assertEquals("No value present!", exception.getMessage());
    }

    @Test
    void url() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Course course = new Course("Test", "Test", 1, "", Optional.empty());
        });

        assertEquals("No value present!", exception.getMessage());
    }

    @Test
    void rejectBlankNotes() {
        assertThrows(IllegalArgumentException.class, () -> new Course("1","title", 1, "url", Optional.of("")));
    }
}