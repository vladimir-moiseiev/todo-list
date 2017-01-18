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

package org.spine3.examples.todolist.validator;

import static org.spine3.examples.todolist.validator.ValidatorHelper.isEmpty;
import static org.spine3.examples.todolist.validator.ValidatorHelper.isNull;

/**
 * @author Illia Shepilov
 */
public class CommonValidator implements Validator {

    private static final String EMPTY_VALUE = "Inserted value cannot be empty.";
    private static final String VALUE_IS_NULL = "Inserted value cannot be null";
    private String message;

    @Override
    public boolean validate(String input) {
        final boolean isNull = isNull(input);
        if (isNull) {
            message = VALUE_IS_NULL;
            return false;
        }

        final boolean isEmpty = isEmpty(input);
        if (isEmpty) {
            message = EMPTY_VALUE;
            return false;
        }

        return true;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
