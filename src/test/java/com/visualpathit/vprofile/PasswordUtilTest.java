package com.visualpathit.vprofile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordUtilTest {
    @Test
    void correctPasswordVerifies() {
        String stored = PasswordUtil.hash("secret123");
        assertTrue(PasswordUtil.verify("secret123", stored));
    }

    @Test
    void wrongPasswordFails() {
        String stored = PasswordUtil.hash("secret123");
        assertFalse(PasswordUtil.verify("wrong-password", stored));
    }

    @Test
    void sameInputGivesDifferentHashes() {
        assertNotEquals(PasswordUtil.hash("secret123"), PasswordUtil.hash("secret123"));
    }

    @Test
    void htmlEscapeNeutralisesTags() {
        assertTrue(Html.escape("<script>").equals("&lt;script&gt;"));
    }
}
