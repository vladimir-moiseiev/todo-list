//
// Copyright 2016, TeamDev Ltd. All rights reserved.
//
// Redistribution and use in source and/or binary forms, with or without
// modification, must retain the above copyright notice and the following
// disclaimer.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
// OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
// SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
// LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
// DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
// THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
// OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
package org.spine3.examples.todolist.aggregate;

import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import org.spine3.examples.todolist.AssignLabelToTask;
import org.spine3.examples.todolist.CompleteTask;
import org.spine3.examples.todolist.CreateBasicTask;
import org.spine3.examples.todolist.CreateDraft;
import org.spine3.examples.todolist.DeleteTask;
import org.spine3.examples.todolist.DeletedTaskRestored;
import org.spine3.examples.todolist.FinalizeDraft;
import org.spine3.examples.todolist.LabelAssignedToTask;
import org.spine3.examples.todolist.LabelRemovedFromTask;
import org.spine3.examples.todolist.RemoveLabelFromTask;
import org.spine3.examples.todolist.ReopenTask;
import org.spine3.examples.todolist.RestoreDeletedTask;
import org.spine3.examples.todolist.Task;
import org.spine3.examples.todolist.TaskCompleted;
import org.spine3.examples.todolist.TaskCreated;
import org.spine3.examples.todolist.TaskDeleted;
import org.spine3.examples.todolist.TaskDescriptionUpdated;
import org.spine3.examples.todolist.TaskDetails;
import org.spine3.examples.todolist.TaskDraftCreated;
import org.spine3.examples.todolist.TaskDraftFinalized;
import org.spine3.examples.todolist.TaskDueDateUpdated;
import org.spine3.examples.todolist.TaskId;
import org.spine3.examples.todolist.TaskPriority;
import org.spine3.examples.todolist.TaskPriorityUpdated;
import org.spine3.examples.todolist.TaskReopened;
import org.spine3.examples.todolist.UpdateTaskDescription;
import org.spine3.examples.todolist.UpdateTaskDueDate;
import org.spine3.examples.todolist.UpdateTaskPriority;
import org.spine3.protobuf.Timestamps;
import org.spine3.server.aggregate.Aggregate;
import org.spine3.server.aggregate.Apply;
import org.spine3.server.command.Assign;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The task aggregate which manages the state of the task.
 *
 * @author Illia Shepilov
 * @see Aggregate
 */
public class TaskAggregate extends Aggregate<TaskId, Task, Task.Builder> {

    /**
     * Creates a new aggregate instance.
     *
     * @param id the ID for the new aggregate.
     * @throws IllegalArgumentException if the ID is not of one of the supported types.
     */
    public TaskAggregate(TaskId id) {
        super(id);
    }

    @Assign
    public List<? extends Message> handle(CreateBasicTask cmd) {
        validateCommand(cmd);
        final TaskCreated result = TaskCreated.newBuilder()
                                              .setDetails(TaskDetails.newBuilder()
                                                                     .setDescription(cmd.getDescription()))
                                              .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(UpdateTaskDescription cmd) {
        validateCommand(cmd);
        final String previousDescription = getState().getDescription();
        final TaskDescriptionUpdated result = TaskDescriptionUpdated.newBuilder()
                                                                    .setId(cmd.getId())
                                                                    .setPreviousDescription(previousDescription)
                                                                    .setNewDescription(cmd.getUpdatedDescription())
                                                                    .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(UpdateTaskDueDate cmd) {
        validateCommand(cmd);
        final Timestamp previousDueDate = getState().getDueDate();
        final TaskDueDateUpdated result = TaskDueDateUpdated.newBuilder()
                                                            .setId(cmd.getId())
                                                            .setPreviousDueDate(previousDueDate)
                                                            .setNewDueDate(cmd.getUpdatedDueDate())
                                                            .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(UpdateTaskPriority cmd) {
        validateCommand(cmd);
        final TaskPriority previousPriority = getState().getPriority();
        final TaskPriorityUpdated result = TaskPriorityUpdated.newBuilder()
                                                              .setId(cmd.getId())
                                                              .setPreviousPriority(previousPriority)
                                                              .setNewPriority(cmd.getUpdatedPriority())
                                                              .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(ReopenTask cmd) {
        validateCommand(cmd);
        final TaskReopened result = TaskReopened.newBuilder()
                                                .setId(cmd.getId())
                                                .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(DeleteTask cmd) {
        validateCommand(cmd);
        final TaskDeleted result = TaskDeleted.newBuilder()
                                              .setId(cmd.getId())
                                              .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(RestoreDeletedTask cmd) {
        validateCommand(cmd);
        final DeletedTaskRestored result = DeletedTaskRestored.newBuilder()
                                                              .setId(cmd.getId())
                                                              .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(CompleteTask cmd) {
        validateCommand(cmd);
        final TaskCompleted result = TaskCompleted.newBuilder()
                                                  .setId(cmd.getId())
                                                  .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(FinalizeDraft cmd) {
        validateCommand(cmd);
        final TaskDraftFinalized result = TaskDraftFinalized.newBuilder()
                                                            .setId(cmd.getId())
                                                            .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(RemoveLabelFromTask cmd) {
        validateCommand(cmd);
        final LabelRemovedFromTask result = LabelRemovedFromTask.newBuilder()
                                                                .setId(cmd.getId())
                                                                .setLabelId(cmd.getLabelId())
                                                                .build();
        return Collections.singletonList(result);
    }

    @Assign
    public List<? extends Message> handle(AssignLabelToTask cmd) {
        validateCommand(cmd);
        final LabelAssignedToTask result = LabelAssignedToTask.newBuilder()
                                                              .setId(cmd.getId())
                                                              .setLabelId(cmd.getLabelId())
                                                              .build();
        return Collections.singletonList(result);
    }

    //TODO[illia.shepilov]: should to be updated after defining draft creation
    @Assign
    public List<? extends Message> handle(CreateDraft cmd) {
        final TaskDraftCreated result = TaskDraftCreated.newBuilder()
                                                        .setDraftCreationTime(Timestamps.getCurrentTime())
                                                        .build();
        return Collections.singletonList(result);
    }

    @Apply
    private void eventOnCreateTask(TaskCreated event) {
        getBuilder().setId(event.getId())
                    .setCreated(Timestamps.getCurrentTime())
                    .setDescription(event.getDetails()
                                         .getDescription())
                    .setPriority(TaskPriority.NORMAL);
    }

    @Apply
    private void eventOnUpdateTaskDescription(TaskDescriptionUpdated event) {
        getBuilder().setId(event.getId())
                    .setDescription(event.getNewDescription());
    }

    @Apply
    private void eventOnUpdateTaskDueDate(TaskDueDateUpdated event) {
        getBuilder().setId(event.getId())
                    .setDueDate(event.getNewDueDate());
    }

    @Apply
    private void eventOnUpdateTaskPriority(TaskPriorityUpdated event) {
        getBuilder().setId(event.getId())
                    .setPriority(event.getNewPriority());
    }

    @Apply
    private void eventOnReopenTask(TaskReopened event) {
        getBuilder().setId(event.getId())
                    .setCompleted(false);
    }

    @Apply
    private void eventOnDeleteTask(TaskDeleted event) {
        getBuilder().setId(event.getId())
                    .setDeleted(true);
    }

    @Apply
    private void eventOnRestoreDeletedTask(DeletedTaskRestored event) {
        getBuilder().setId(event.getId())
                    .setDeleted(false);
    }

    @Apply
    private void eventOnCompleteTask(TaskCompleted event) {
        getBuilder().setId(event.getId())
                    .setCompleted(true);
    }

    @Apply
    private void eventOnTaskDetails(TaskDetails event) {
        getBuilder().setPriority(event.getPriority())
                    .setDescription(event.getDescription())
                    .setCompleted(event.getCompleted());
    }

    @Apply
    private void eventOnFinalizeTaskDraft(TaskDraftFinalized event) {
        getBuilder().setId(event.getId())
                    .setDraft(false);
    }

    @Apply
    private void eventOnAssignLabelToTask(LabelAssignedToTask event) {
        getBuilder().setId(event.getId())
                    .addLabelIds(event.getLabelId());
    }

    @Apply
    private void eventOnRemoveLabelFromTask(LabelRemovedFromTask event) {
        getBuilder().setId(event.getId())
                    .addLabelIds(event.getLabelId());
    }

    //TODO[illia.shepilov]: should to be updated after defining draft creation
    @Apply
    private void eventOnCreateTaskDraft(TaskDraftCreated event) {
        getBuilder().setId(event.getId())
                    .setCreated(event.getDraftCreationTime())
                    .setDescription(event.getDetails()
                                         .getDescription())
                    .setDraft(true);
    }

    //TODO[illia.shepilov]: Need clarification
    // is it good idea to pass command object to the method
    // when it is not needed?
    // It is doing for using one namespace ("validateCommand")

    private void isCommandCompletedCheck() {
        if (getState().getCompleted()) {
            throw new IllegalArgumentException("Command cannot be applied on completed task.");
        }
    }

    private void isCommandDeletedCheck() {
        if (getState().getDeleted()) {
            throw new IllegalArgumentException("Command cannot be applied on deleted task.");
        }
    }

    private void validateCommand(AssignLabelToTask cmd) {
        isCommandDeletedCheck();
        isCommandCompletedCheck();
    }

    private void validateCommand(RemoveLabelFromTask cmd) {
        isCommandDeletedCheck();
        isCommandCompletedCheck();
    }

    private void validateCommand(UpdateTaskDueDate cmd) {
        isCommandDeletedCheck();
        isCommandCompletedCheck();
    }

    private void validateCommand(UpdateTaskPriority cmd) {
        isCommandDeletedCheck();
        isCommandCompletedCheck();
    }

    private void validateCommand(CreateBasicTask cmd) {
        final String description = cmd.getDescription();
        if (description != null && description.length() < 3) {
            throw new IllegalArgumentException("Description should contains at least 3 alphanumeric symbols");
        }
    }

    private void validateCommand(UpdateTaskDescription cmd) {
        final String description = cmd.getUpdatedDescription();

        checkNotNull(description, "Description cannot be null.");

        if (description.length() < 3) {
            throw new IllegalArgumentException("Description should contains at least 3 alphanumeric symbols.");
        }

        isCommandDeletedCheck();
        isCommandCompletedCheck();
    }

    private void validateCommand(RestoreDeletedTask cmd) {
        if (!getState().getDeleted()) {
            throw new IllegalArgumentException("Command cannot be applied on not deleted task.");
        }
    }

    private void validateCommand(DeleteTask cmd) {
        isCommandDeletedCheck();
    }

    private void validateCommand(CompleteTask cmd) {
        isCommandDeletedCheck();

        if (getState().getDraft()) {
            throw new IllegalArgumentException("Command cannot be applied on task in draft state.");
        }
    }

    private void validateCommand(ReopenTask cmd) {
        if (!getState().getCompleted()) {
            throw new IllegalArgumentException("Command cannot be applied on not completed task.");
        }
    }

    private void validateCommand(FinalizeDraft cmd) {
        final Task state = getState();
        if (state.getDeleted()) {
            throw new IllegalArgumentException("Command cannot be applied to the deleted draft.");
        }
        if (!state.getDraft()) {
            throw new IllegalArgumentException("Command applicable to the draft tasks only.");
        }
    }

}
