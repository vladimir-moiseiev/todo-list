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

package org.spine3.examples.todolist.modes;

import asg.cliche.Command;
import org.spine3.examples.todolist.client.TodoClient;
import org.spine3.examples.todolist.q.projections.LabelledTasksView;

import java.io.BufferedReader;
import java.util.List;

import static org.spine3.examples.todolist.modes.LabelledTasksMode.LabelledTasksModeConstants.EMPTY_LABELLED_TASKS;
import static org.spine3.examples.todolist.modes.LabelledTasksMode.LabelledTasksModeConstants.HELP_MESSAGE;
import static org.spine3.examples.todolist.modes.ModeHelper.constructUserFriendlyLabelledTasks;
import static org.spine3.examples.todolist.modes.ModeHelper.sendMessageToUser;

/**
 * @author Illia Shepilov
 */
@SuppressWarnings("unused")
public class LabelledTasksMode extends CommonMode {

    LabelledTasksMode(TodoClient client, BufferedReader reader) {
        super(client, reader);
    }

    @Command
    public void help() {
        sendMessageToUser(HELP_MESSAGE);
    }

    @Command(abbrev = "1")
    public void obtainLabelledTasksView() {
        final List<LabelledTasksView> labelledTasks = client.getLabelledTasksView();
        final String message = labelledTasks.isEmpty() ? EMPTY_LABELLED_TASKS :
                               constructUserFriendlyLabelledTasks(labelledTasks);
        sendMessageToUser(message);
    }

    static class LabelledTasksModeConstants {
        static final String EMPTY_LABELLED_TASKS = "No labelled tasks.";
        static final String HELP_MESSAGE = "0:    Help.\n" +
                "1:    Show the labelled tasks.\n" +
                CommonMode.CommonModeConstants.HELP_MESSAGE +
                "exit: Exit from the mode.";
    }
}
