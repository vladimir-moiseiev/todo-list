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

package org.spine3.examples.todolist.c.aggregate.failures;

import org.spine3.base.CommandContext;
import org.spine3.change.ValueMismatch;
import org.spine3.examples.todolist.CompleteTaskFailed;
import org.spine3.examples.todolist.CreateBasicTaskFailed;
import org.spine3.examples.todolist.CreateDraftFailed;
import org.spine3.examples.todolist.DeleteTaskFailed;
import org.spine3.examples.todolist.DescriptionUpdateFailed;
import org.spine3.examples.todolist.FailedTaskCommandDetails;
import org.spine3.examples.todolist.FinalizeDraftFailed;
import org.spine3.examples.todolist.PriorityUpdateFailed;
import org.spine3.examples.todolist.ReopenTaskFailed;
import org.spine3.examples.todolist.RestoreDeletedTaskFailed;
import org.spine3.examples.todolist.TaskDueDateUpdateFailed;
import org.spine3.examples.todolist.c.aggregate.TaskDefinitionPart;
import org.spine3.examples.todolist.c.commands.CompleteTask;
import org.spine3.examples.todolist.c.commands.CreateBasicTask;
import org.spine3.examples.todolist.c.commands.CreateDraft;
import org.spine3.examples.todolist.c.commands.DeleteTask;
import org.spine3.examples.todolist.c.commands.FinalizeDraft;
import org.spine3.examples.todolist.c.commands.ReopenTask;
import org.spine3.examples.todolist.c.commands.RestoreDeletedTask;
import org.spine3.examples.todolist.c.commands.UpdateTaskDescription;
import org.spine3.examples.todolist.c.commands.UpdateTaskDueDate;
import org.spine3.examples.todolist.c.commands.UpdateTaskPriority;
import org.spine3.examples.todolist.c.failures.CannotCompleteTask;
import org.spine3.examples.todolist.c.failures.CannotCreateDraft;
import org.spine3.examples.todolist.c.failures.CannotCreateTaskWithInappropriateDescription;
import org.spine3.examples.todolist.c.failures.CannotDeleteTask;
import org.spine3.examples.todolist.c.failures.CannotFinalizeDraft;
import org.spine3.examples.todolist.c.failures.CannotReopenTask;
import org.spine3.examples.todolist.c.failures.CannotRestoreDeletedTask;
import org.spine3.examples.todolist.c.failures.CannotUpdateTaskDescription;
import org.spine3.examples.todolist.c.failures.CannotUpdateTaskDueDate;
import org.spine3.examples.todolist.c.failures.CannotUpdateTaskPriority;
import org.spine3.examples.todolist.c.failures.CannotUpdateTaskWithInappropriateDescription;

/**
 * Utility class for working with {@link TaskDefinitionPart} failures.
 *
 * @author Illia Shepilov
 */
public class TaskDefinitionPartFailures {

    private TaskDefinitionPartFailures() {
    }

    public static class UpdateFailures {

        private UpdateFailures() {
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskDueDate} failure according to the passed parameters.
         *
         * @param cmd the {@code UpdateTaskDueDate} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotUpdateTaskDueDate the failure to throw
         */
        public static void throwCannotUpdateTaskDueDateFailure(UpdateTaskDueDate cmd,
                                                               CommandContext ctx) throws CannotUpdateTaskDueDate {
            final FailedTaskCommandDetails commandFailed =
                    FailedTaskCommandDetails.newBuilder()
                                            .setTaskId(cmd.getId())
                                            .build();
            final TaskDueDateUpdateFailed dueDateUpdateFailed =
                    TaskDueDateUpdateFailed.newBuilder()
                                           .setFailureDetails(commandFailed)
                                           .build();
            throw new CannotUpdateTaskDueDate(cmd, ctx, dueDateUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskDescription} failure according to the passed parameters.
         *
         * @param cmd      the {@code UpdateTaskDescription} command which thrown the failure
         * @param ctx      the {@code CommandContext}
         * @param mismatch the {@link ValueMismatch}
         * @throws CannotUpdateTaskDescription the failure to throw
         */
        public static void throwCannotUpdateTaskDescriptionFailure(UpdateTaskDescription cmd,
                                                                   CommandContext ctx,
                                                                   ValueMismatch mismatch)
                throws CannotUpdateTaskDescription {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final DescriptionUpdateFailed descriptionUpdateFailed =
                    DescriptionUpdateFailed.newBuilder()
                                           .setFailureDetails(commandFailed)
                                           .setDescriptionMismatch(mismatch)
                                           .build();
            throw new CannotUpdateTaskDescription(cmd, ctx, descriptionUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskDescription} failure according to the passed parameters.
         *
         * @param cmd the {@code UpdateTaskDescription} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotUpdateTaskDescription the failure to throw
         */
        public static void throwCannotUpdateTaskDescriptionFailure(UpdateTaskDescription cmd, CommandContext ctx)
                throws CannotUpdateTaskDescription {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final DescriptionUpdateFailed descriptionUpdateFailed =
                    DescriptionUpdateFailed.newBuilder()
                                           .setFailureDetails(commandFailed)
                                           .build();
            throw new CannotUpdateTaskDescription(cmd, ctx, descriptionUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskDueDate} failure according to the passed parameters.
         *
         * @param cmd      the {@code UpdateTaskDueDate} command which thrown the failure
         * @param ctx      the {@code CommandContext}
         * @param mismatch the {@link ValueMismatch}
         * @throws CannotUpdateTaskDueDate the failure to throw
         */
        public static void throwCannotUpdateTaskDueDateFailure(UpdateTaskDueDate cmd,
                                                               CommandContext ctx,
                                                               ValueMismatch mismatch) throws CannotUpdateTaskDueDate {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final TaskDueDateUpdateFailed dueDateUpdateFailed =
                    TaskDueDateUpdateFailed.newBuilder()
                                           .setFailureDetails(commandFailed)
                                           .setDueDateMismatch(mismatch)
                                           .build();
            throw new CannotUpdateTaskDueDate(cmd, ctx, dueDateUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskDescription} failure according to the passed parameters.
         *
         * @param cmd      the {@code UpdateTaskDescription} command which thrown the failure
         * @param ctx      the {@code CommandContext}
         * @param mismatch the {@link ValueMismatch}
         * @throws CannotUpdateTaskDescription the failure to throw
         */
        public static void throwCannotUpdateDescriptionFailure(UpdateTaskDescription cmd,
                                                               CommandContext ctx,
                                                               ValueMismatch mismatch)
                throws CannotUpdateTaskDescription {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final DescriptionUpdateFailed descriptionUpdateFailed =
                    DescriptionUpdateFailed.newBuilder()
                                           .setFailureDetails(commandFailed)
                                           .setDescriptionMismatch(mismatch)
                                           .build();
            throw new CannotUpdateTaskDescription(cmd, ctx, descriptionUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskPriority} failure according to the passed parameters.
         *
         * @param cmd      the {@code UpdateTaskPriority} command which thrown the failure
         * @param ctx      the {@code CommandContext}
         * @param mismatch the {@link ValueMismatch}
         * @throws CannotUpdateTaskPriority the failure to throw
         */
        public static void throwCannotUpdateTaskPriorityFailure(UpdateTaskPriority cmd,
                                                                CommandContext ctx,
                                                                ValueMismatch mismatch) throws CannotUpdateTaskPriority {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final PriorityUpdateFailed priorityUpdateFailed = PriorityUpdateFailed.newBuilder()
                                                                                  .setFailureDetails(commandFailed)
                                                                                  .setPriorityMismatch(mismatch)
                                                                                  .build();
            throw new CannotUpdateTaskPriority(cmd, ctx, priorityUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskPriority} failure according to the passed parameters.
         *
         * @param cmd the {@code UpdateTaskPriority} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotUpdateTaskPriority the failure to throw
         */
        public static void throwCannotUpdateTaskPriorityFailure(UpdateTaskPriority cmd, CommandContext ctx)
                throws CannotUpdateTaskPriority {
            final FailedTaskCommandDetails commandFailed =
                    FailedTaskCommandDetails.newBuilder()
                                            .setTaskId(cmd.getId())
                                            .build();
            final PriorityUpdateFailed priorityUpdateFailed = PriorityUpdateFailed.newBuilder()
                                                                                  .setFailureDetails(commandFailed)
                                                                                  .build();
            throw new CannotUpdateTaskPriority(cmd, ctx, priorityUpdateFailed);
        }

        /**
         * Constructs and throws the {@link CannotUpdateTaskWithInappropriateDescription} failure
         * according to the passed parameters.
         *
         * @param cmd the {@code UpdateTaskDescription} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotUpdateTaskWithInappropriateDescription the failure to throw
         */
        public static void throwCannotUpdateTooShortDescriptionFailure(UpdateTaskDescription cmd, CommandContext ctx)
                throws CannotUpdateTaskWithInappropriateDescription {
            final FailedTaskCommandDetails commandFailed =
                    FailedTaskCommandDetails.newBuilder()
                                            .setTaskId(cmd.getId())
                                            .build();
            final DescriptionUpdateFailed updateFailed = DescriptionUpdateFailed.newBuilder()
                                                                                .setFailureDetails(commandFailed)
                                                                                .build();
            throw new CannotUpdateTaskWithInappropriateDescription(cmd, ctx, updateFailed);
        }
    }

    public static class TaskCreationFailures {

        private TaskCreationFailures() {
        }

        /**
         * Constructs and throws the {@link CannotCreateDraft} failure according to the passed parameters.
         *
         * @param cmd the {@code CreateDraft} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotCreateDraft the failure to throw
         */
        public static void throwCannotCreateDraftFailure(CreateDraft cmd, CommandContext ctx) throws CannotCreateDraft {
            final FailedTaskCommandDetails commandFailed =
                    FailedTaskCommandDetails.newBuilder()
                                            .setTaskId(cmd.getId())
                                            .build();
            final CreateDraftFailed createDraftFailed = CreateDraftFailed.newBuilder()
                                                                         .setFailureDetails(commandFailed)
                                                                         .build();
            throw new CannotCreateDraft(cmd, ctx, createDraftFailed);
        }

        /**
         * Constructs and throws the {@link CannotCreateTaskWithInappropriateDescription} failure
         * according to the passed parameters.
         *
         * @param cmd the {@code CreateBasicTask} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotCreateTaskWithInappropriateDescription the failure to throw
         */
        public static void throwCannotCreateTaskWithInappropriateDescriptionFailure(CreateBasicTask cmd,
                                                                                    CommandContext ctx)
                throws CannotCreateTaskWithInappropriateDescription {
            final FailedTaskCommandDetails commandFailed =
                    FailedTaskCommandDetails.newBuilder()
                                            .setTaskId(cmd.getId())
                                            .build();
            final CreateBasicTaskFailed createBasicTaskFailed = CreateBasicTaskFailed.newBuilder()
                                                                                     .setFailureDetails(commandFailed)
                                                                                     .build();
            throw new CannotCreateTaskWithInappropriateDescription(cmd, ctx, createBasicTaskFailed);
        }
    }

    public static class ChangeStatusFailures {

        private ChangeStatusFailures() {
        }

        /**
         * Constructs and throws the {@link CannotReopenTask} failure according to the passed parameters.
         *
         * @param cmd the {@code ReopenTask} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotReopenTask the failure to throw
         */
        public static void throwCannotReopenTaskFailure(ReopenTask cmd, CommandContext ctx) throws CannotReopenTask {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final ReopenTaskFailed reopenTaskFailed = ReopenTaskFailed.newBuilder()
                                                                      .setFailureDetails(commandFailed)
                                                                      .build();
            throw new CannotReopenTask(cmd, ctx, reopenTaskFailed);
        }

        /**
         * Constructs and throws the {@link CannotFinalizeDraft} failure according to the passed parameters.
         *
         * @param cmd the {@code FinalizeDraft} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotFinalizeDraft the failure to throw
         */
        public static void throwCannotFinalizeDraftFailure(FinalizeDraft cmd, CommandContext ctx)
                throws CannotFinalizeDraft {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final FinalizeDraftFailed finalizeDraftFailed = FinalizeDraftFailed.newBuilder()
                                                                               .setFailureDetails(commandFailed)
                                                                               .build();
            throw new CannotFinalizeDraft(cmd, ctx, finalizeDraftFailed);
        }

        /**
         * Constructs and throws the {@link CannotDeleteTask} failure according to the passed parameters.
         *
         * @param cmd the {@code DeleteTask} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotDeleteTask the failure to throw
         */
        public static void throwCannotDeleteTaskFailure(DeleteTask cmd, CommandContext ctx) throws CannotDeleteTask {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final DeleteTaskFailed deleteTaskFailed = DeleteTaskFailed.newBuilder()
                                                                      .setFailureDetails(commandFailed)
                                                                      .build();
            throw new CannotDeleteTask(cmd, ctx, deleteTaskFailed);
        }

        /**
         * Constructs and throws the {@link CannotCompleteTask} failure according to the passed parameters.
         *
         * @param cmd the {@code CompleteTask} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotCompleteTask the failure to throw
         */
        public static void throwCannotCompleteTaskFailure(CompleteTask cmd,
                                                          CommandContext ctx) throws CannotCompleteTask {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final CompleteTaskFailed completeTaskFailed = CompleteTaskFailed.newBuilder()
                                                                            .setFailureDetails(commandFailed)
                                                                            .build();
            throw new CannotCompleteTask(cmd, ctx, completeTaskFailed);
        }

        /**
         * Constructs and throws the {@link CannotRestoreDeletedTask} failure according to the passed parameters.
         *
         * @param cmd the {@code RestoreDeletedTask} command which thrown the failure
         * @param ctx the {@code CommandContext}
         * @throws CannotRestoreDeletedTask the {@code CannotRestoreDeletedTask} failure
         */
        public static void throwCannotRestoreDeletedTaskFailure(RestoreDeletedTask cmd,
                                                                CommandContext ctx) throws CannotRestoreDeletedTask {
            final FailedTaskCommandDetails commandFailed = FailedTaskCommandDetails.newBuilder()
                                                                                   .setTaskId(cmd.getId())
                                                                                   .build();
            final RestoreDeletedTaskFailed restoreDeletedTaskFailed =
                    RestoreDeletedTaskFailed.newBuilder()
                                            .setFailureDetails(commandFailed)
                                            .build();
            throw new CannotRestoreDeletedTask(cmd, ctx, restoreDeletedTaskFailed);
        }
    }
}