/*
 * Copyright 2017, TeamDev Ltd. All rights reserved.
 *
 * Redistribution and use in source and/or binary forms, with or without
 * modification, must retain the above copyright notice and the following
 * disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package io.spine.cli.action;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Dmytro Grankin
 */
@DisplayName("Shortcut should")
class ShortcutTest {

    private static final String VALUE = "s";
    private static final Shortcut SHORTCUT = new Shortcut(VALUE);

    @Test
    @DisplayName("not allow empty value")
    void notAllowEmptyValue() {
        assertThrows(IllegalArgumentException.class, () -> new Shortcut(""));
    }

    @Test
    @DisplayName("consider a shortcut with same value equal")
    void considerShortcutWithSameValueEqual() {
        assertEquals(SHORTCUT, SHORTCUT);
        assertEquals(SHORTCUT.hashCode(), SHORTCUT.hashCode());
    }

    @SuppressWarnings("ObjectEqualsNull") // Purpose of this test.
    @Test
    @DisplayName("consider other classes not equal")
    void considerOtherClassesNotEqual() {
        assertFalse(SHORTCUT.equals(null)); // `assertNotEquals` does not suit for null comparison.
        assertNotEquals(VALUE, SHORTCUT);
    }

    @Test
    @DisplayName("return a value when toString is called")
    void overrideToString() {
        final Shortcut shortcut = new Shortcut(VALUE);
        assertEquals(VALUE, shortcut.toString());
    }
}
