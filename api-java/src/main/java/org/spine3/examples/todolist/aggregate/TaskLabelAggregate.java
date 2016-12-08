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

import org.spine3.examples.todolist.AssignLabelToTask;
import org.spine3.examples.todolist.CreateBasicLabel;
import org.spine3.examples.todolist.LabelAssignedToTask;
import org.spine3.examples.todolist.LabelColor;
import org.spine3.examples.todolist.LabelCreated;
import org.spine3.examples.todolist.LabelDetails;
import org.spine3.examples.todolist.LabelDetailsUpdated;
import org.spine3.examples.todolist.LabelRemovedFromTask;
import org.spine3.examples.todolist.RemoveLabelFromTask;
import org.spine3.examples.todolist.TaskLabel;
import org.spine3.examples.todolist.TaskLabelId;
import org.spine3.examples.todolist.UpdateLabelDetails;
import org.spine3.server.aggregate.Aggregate;
import org.spine3.server.aggregate.Apply;
import org.spine3.server.command.Assign;

public class TaskLabelAggregate extends Aggregate<TaskLabelId, TaskLabel, TaskLabel.Builder> {

    /**
     * Creates a new aggregate instance.
     *
     * @param id the ID for the new aggregate
     * @throws IllegalArgumentException if the ID is not of one of the supported types
     */
    public TaskLabelAggregate(TaskLabelId id) {
        super(id);
    }

    @Assign
    public LabelAssignedToTask handle(AssignLabelToTask cmd) {
        final LabelAssignedToTask result = LabelAssignedToTask.newBuilder()
                                                              .setId(cmd.getId())
                                                              .setLabelId(cmd.getLabelId())
                                                              .build();
        return result;
    }

    @Assign
    public LabelRemovedFromTask handle(RemoveLabelFromTask cmd) {
        final LabelRemovedFromTask result = LabelRemovedFromTask.newBuilder()
                                                                .setId(cmd.getId())
                                                                .setLabelId(cmd.getLabelId())
                                                                .build();
        return result;
    }

    @Assign
    public LabelCreated handle(CreateBasicLabel cmd) {
        final LabelCreated result = LabelCreated.newBuilder()
                                                .setDetails(LabelDetails.newBuilder()
                                                                        .setTitle(cmd.getLabelTitle()))
                                                .build();
        return result;
    }

    @Assign
    public LabelDetailsUpdated handle(UpdateLabelDetails cmd) {
        final LabelDetailsUpdated result = LabelDetailsUpdated.newBuilder()
                                                              .setId(cmd.getId())
                                                              .setPreviousDetails(LabelDetails.newBuilder()
                                                                                              .setColor(getState().getColor())
                                                                                              .setTitle(getState().getTitle()))
                                                              .setNewDetails(LabelDetails.newBuilder()
                                                                                         .setTitle(cmd.getNewTitle())
                                                                                         .setColor(cmd.getColor()))
                                                              .build();
        return result;
    }

    @Apply
    private void eventOnAssignedLabelToTask(LabelAssignedToTask event) {
        getBuilder().setId(event.getLabelId());
    }

    @Apply
    private void eventOnRemoveLabelFromTask(LabelRemovedFromTask event) {
        getBuilder().setId(event.getLabelId());
    }

    @Apply
    private void eventOnCreateLabel(LabelCreated event) {
        getBuilder().setId(event.getId())
                    .setTitle(event.getDetails()
                                   .getTitle())
                    .setColor(LabelColor.GRAY);
    }

    @Apply
    private void eventOnUpdateLabelDetails(LabelDetailsUpdated event) {
        LabelDetails labelDetails = event.getNewDetails();
        getBuilder().setId(event.getId())
                    .setTitle(labelDetails.getTitle())
                    .setColor(labelDetails.getColor());
    }

    @Apply
    private void eventOnLabelDetails(LabelDetails event) {
        getBuilder().setTitle(event.getTitle())
                    .setColor(event.getColor());
    }

}