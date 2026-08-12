package com.balajimart.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    @DisplayName("Should return true for non-empty strings")
    void testIsNotEmptyValid() {
        assertTrue(ValidationUtil.isNotEmpty("Hello"));
        assertTrue(ValidationUtil.isNotEmpty("  World  "));
    }

    @Test
    @DisplayName("Should return false for null or empty/whitespace strings")
    void testIsNotEmptyInvalid() {
        assertFalse(ValidationUtil.isNotEmpty(null));
        assertFalse(ValidationUtil.isNotEmpty(""));
        assertFalse(ValidationUtil.isNotEmpty("   "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"user@example.com", "test.user@domain.co.in", "admin+tag@sub.domain.org"})
    @DisplayName("Should return true for valid email formats")
    void testIsValidEmailTrue(String email) {
        assertTrue(ValidationUtil.isValidEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"plainaddress", "missing@domain", "@no-local.com", "space in@email.com", ""})
    @DisplayName("Should return false for invalid email formats")
    void testIsValidEmailFalse(String email) {
        assertFalse(ValidationUtil.isValidEmail(email));
    }
}
