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

import jline.console.ConsoleReader;
import org.spine3.examples.todolist.TaskId;
import org.spine3.examples.todolist.c.commands.FinalizeDraft;
import org.spine3.examples.todolist.client.TodoClient;
import org.spine3.examples.todolist.q.projections.DraftTasksView;

import java.io.IOException;

import static org.spine3.examples.todolist.modes.DraftTasksMode.DraftTasksModeConstants.DRAFT_FINALIZED_MESSAGE;
import static org.spine3.examples.todolist.modes.DraftTasksMode.DraftTasksModeConstants.EMPTY_DRAFT_TASKS;
import static org.spine3.examples.todolist.modes.DraftTasksMode.DraftTasksModeConstants.HELP_MESSAGE;
import static org.spine3.examples.todolist.modes.Mode.ModeConstants.BACK;
import static org.spine3.examples.todolist.modes.ModeHelper.constructUserFriendlyDraftTasks;
import static org.spine3.examples.todolist.modes.ModeHelper.sendMessageToUser;

/**
 * @author Illia Shepilov
 */
@SuppressWarnings("unused")
public class DraftTasksMode extends CommonMode {

    DraftTasksMode(TodoClient client, ConsoleReader reader) {
        super(client, reader);
        modeMap.put("1", new ShowDraftTasksMode(client, reader));
    }

    @Override
    void start() throws IOException {
        modeMap.get("1")
               .start();
        sendMessageToUser(HELP_MESSAGE);
        String line = reader.readLine();
        while (!line.equals(BACK)) {
            line = reader.readLine();
            final Mode mode = modeMap.get(line);
            if (mode != null) {
                mode.start();
            }
        }
    }

    private static class ShowDraftTasksMode extends Mode {

        private ShowDraftTasksMode(TodoClient client, ConsoleReader reader) {
            super(client, reader);
        }

        @Override
        void start() throws IOException {
            final DraftTasksView draftTasksView = client.getDraftTasksView();
            final int itemsCount = draftTasksView.getDraftTasks()
                                                 .getItemsCount();
            final boolean isEmpty = itemsCount == 0;
            final String message = isEmpty ? EMPTY_DRAFT_TASKS : constructUserFriendlyDraftTasks(draftTasksView);
            sendMessageToUser(message);
        }
    }

    private static class FinalizeDraftMode extends Mode {
        private FinalizeDraftMode(TodoClient client, ConsoleReader reader) {
            super(client, reader);
        }

        @Override
        void start() throws IOException {
            final String idValue = obtainTaskIdValue();
            final TaskId taskId = TaskId.newBuilder()
                                        .setValue(idValue)
                                        .build();
            final FinalizeDraft finalizeDraft = FinalizeDraft.newBuilder()
                                                             .setId(taskId)
                                                             .build();
            client.finalize(finalizeDraft);
            final String message = String.format(DRAFT_FINALIZED_MESSAGE, idValue);
            sendMessageToUser(message);
        }
    }

    static class DraftTasksModeConstants {
        static final String EMPTY_DRAFT_TASKS = "No draft tasks.";
        static final String DRAFT_FINALIZED_MESSAGE = "Task with id value: %s finalized.";
        static final String HELP_MESSAGE = "0:    Help.\n" +
                "1:    Show the tasks in the draft state.\n" +
                CommonMode.CommonModeConstants.HELP_MESSAGE +
                "12:   Finalize the draft.\n" +
                "exit: Exit from the mode.";

        private DraftTasksModeConstants() {
        }
    }
}
