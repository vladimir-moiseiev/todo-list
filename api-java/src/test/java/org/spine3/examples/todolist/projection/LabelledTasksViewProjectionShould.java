/*
 * Copyright 2016, TeamDev Ltd. All rights reserved.
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

package org.spine3.examples.todolist.projection;

import com.google.protobuf.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spine3.base.Event;
import org.spine3.base.EventContext;
import org.spine3.examples.todolist.DeletedTaskRestored;
import org.spine3.examples.todolist.LabelAssignedToTask;
import org.spine3.examples.todolist.LabelColor;
import org.spine3.examples.todolist.LabelRemovedFromTask;
import org.spine3.examples.todolist.TaskCompleted;
import org.spine3.examples.todolist.TaskDeleted;
import org.spine3.examples.todolist.TaskDescriptionUpdated;
import org.spine3.examples.todolist.TaskDueDateUpdated;
import org.spine3.examples.todolist.TaskId;
import org.spine3.examples.todolist.TaskLabelId;
import org.spine3.examples.todolist.TaskPriorityUpdated;
import org.spine3.examples.todolist.TaskReopened;
import org.spine3.examples.todolist.view.LabelledTasksView;
import org.spine3.examples.todolist.view.TaskListView;
import org.spine3.examples.todolist.view.TaskView;
import org.spine3.server.BoundedContext;
import org.spine3.server.event.EventBus;
import org.spine3.server.event.enrich.EventEnricher;
import org.spine3.server.projection.ProjectionRepository;
import org.spine3.server.storage.memory.InMemoryStorageFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.spine3.base.Events.createEvent;
import static org.spine3.examples.todolist.testdata.TestBoundedContextFactory.boundedContextInstance;
import static org.spine3.examples.todolist.testdata.TestEventContextFactory.eventContextInstance;
import static org.spine3.examples.todolist.testdata.TestEventEnricherFactory.LABEL_TITLE;
import static org.spine3.examples.todolist.testdata.TestEventEnricherFactory.eventEnricherInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.LABEL_ID;
import static org.spine3.examples.todolist.testdata.TestEventFactory.TASK_ID;
import static org.spine3.examples.todolist.testdata.TestEventFactory.UPDATED_DESCRIPTION;
import static org.spine3.examples.todolist.testdata.TestEventFactory.UPDATED_TASK_DUE_DATE;
import static org.spine3.examples.todolist.testdata.TestEventFactory.UPDATED_TASK_PRIORITY;
import static org.spine3.examples.todolist.testdata.TestEventFactory.deletedTaskRestoredInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.labelAssignedToTaskInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.labelRemovedFromTaskInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.taskCompletedInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.taskDeletedInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.taskDescriptionUpdatedInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.taskDueDateUpdatedInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.taskPriorityUpdatedInstance;
import static org.spine3.examples.todolist.testdata.TestEventFactory.taskReopenedInstance;

/**
 * @author Illia Shepilov
 */
public class LabelledTasksViewProjectionShould {

    private Event labelAssignedToTaskEvent;
    private Event labelRemovedFromTaskEvent;
    private Event deletedTaskRestoredEvent;
    private Event deletedTaskEvent;
    private Event taskDescriptionUpdatedEvent;
    private Event taskPriorityUpdatedEvent;
    private Event taskDueDateUpdatedEvent;
    private Event taskCompletedEvent;
    private Event taskReopenedEvent;
    private EventBus eventBus;
    private EventContext eventContext = eventContextInstance();
    private ProjectionRepository<TaskLabelId, LabelledTasksViewProjection, LabelledTasksView> repository;

    @BeforeEach
    public void setUp() {
        final InMemoryStorageFactory storageFactory = InMemoryStorageFactory.getInstance();
        final EventEnricher eventEnricher = eventEnricherInstance();
        final BoundedContext boundedContext = boundedContextInstance(eventEnricher, storageFactory);
        repository = new LabelledTasksViewProjectionRepository(boundedContext);
        repository.initStorage(storageFactory);
        repository.setOnline();
        boundedContext.register(repository);
        eventBus = boundedContext.getEventBus();
        eventContext = eventContextInstance();
        final LabelAssignedToTask labelAssignedToTask = labelAssignedToTaskInstance();
        final LabelRemovedFromTask labelRemovedFromTask = labelRemovedFromTaskInstance();
        final DeletedTaskRestored deletedTaskRestored = deletedTaskRestoredInstance();
        final TaskDeleted taskDeleted = taskDeletedInstance();
        final TaskDescriptionUpdated taskDescriptionUpdated = taskDescriptionUpdatedInstance();
        final TaskPriorityUpdated taskPriorityUpdated = taskPriorityUpdatedInstance();
        final TaskDueDateUpdated taskDueDateUpdated = taskDueDateUpdatedInstance();
        final TaskCompleted taskCompleted = taskCompletedInstance();
        final TaskReopened taskReopened = taskReopenedInstance();
        labelAssignedToTaskEvent = createEvent(labelAssignedToTask, eventContext);
        labelRemovedFromTaskEvent = createEvent(labelRemovedFromTask, eventContext);
        deletedTaskRestoredEvent = createEvent(deletedTaskRestored, eventContext);
        deletedTaskEvent = createEvent(taskDeleted, eventContext);
        taskDescriptionUpdatedEvent = createEvent(taskDescriptionUpdated, eventContext);
        taskPriorityUpdatedEvent = createEvent(taskPriorityUpdated, eventContext);
        taskDueDateUpdatedEvent = createEvent(taskDueDateUpdated, eventContext);
        taskCompletedEvent = createEvent(taskCompleted, eventContext);
        taskReopenedEvent = createEvent(taskReopened, eventContext);
    }

    @Test
    public void add_task_view_to_state_when_label_assigned_to_task() {
        int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        LabelledTasksView labelledTaskView = repository.load(LABEL_ID)
                                                       .getState();
        TaskListView listView = labelledTaskView.getLabelledTasks();

        matchedExpectedValues(labelledTaskView);

        int actualListSize = listView.getItemsCount();
        assertEquals(expectedListSize, actualListSize);

        TaskView view = listView.getItems(0);

        matchedExpectedValues(view);

        eventBus.post(labelAssignedToTaskEvent);
        expectedListSize = 2;
        labelledTaskView = repository.load(LABEL_ID)
                                     .getState();
        listView = labelledTaskView.getLabelledTasks();
        actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        view = listView.getItems(0);

        matchedExpectedValues(view);
        matchedExpectedValues(labelledTaskView);

        view = listView.getItems(1);

        matchedExpectedValues(view);
        matchedExpectedValues(labelledTaskView);
    }

    @Test
    public void remove_task_view_from_state_when_label_removed_from_task() {
        int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(labelRemovedFromTaskEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);

        TaskListView labelledTasks = labelledTasksView.getLabelledTasks();
        int actualListSize = labelledTasks.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = labelledTasks.getItems(0);

        assertEquals(LABEL_ID, taskView.getLabelId());
        assertEquals(TASK_ID, taskView.getId());

        eventBus.post(labelRemovedFromTaskEvent);
        expectedListSize = 0;
        labelledTasks = repository.load(LABEL_ID)
                                  .getState()
                                  .getLabelledTasks();
        actualListSize = labelledTasks.getItemsCount();

        assertEquals(expectedListSize, actualListSize);
        assertTrue(labelledTasks.getItemsList()
                                .isEmpty());
    }

    @Test
    public void remove_task_view_from_state_when_deleted_task_is_restored() {
        int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(labelRemovedFromTaskEvent);
        eventBus.post(labelRemovedFromTaskEvent);
        eventBus.post(deletedTaskRestoredEvent);
        LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                        .getState();
        matchedExpectedValues(labelledTasksView);

        TaskListView listView = labelledTasksView.getLabelledTasks();
        int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        eventBus.post(deletedTaskRestoredEvent);
        final TaskView taskView = listView.getItems(0);

        assertEquals(TASK_ID, taskView.getId());

        expectedListSize = 2;
        labelledTasksView = repository.load(LABEL_ID)
                                      .getState();

        matchedExpectedValues(labelledTasksView);

        listView = repository.load(LABEL_ID)
                             .getState()
                             .getLabelledTasks();
        actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);
        assertEquals(TASK_ID, listView.getItems(0)
                                      .getId());
        assertEquals(TASK_ID, listView.getItems(1)
                                      .getId());

    }

    @Test
    public void remove_task_view_from_state_when_task_is_deleted() {
        int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(deletedTaskEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);

        TaskListView listView = labelledTasksView.getLabelledTasks();
        int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        matchedExpectedValues(taskView);

        eventBus.post(deletedTaskEvent);
        expectedListSize = 0;
        listView = repository.load(LABEL_ID)
                             .getState()
                             .getLabelledTasks();
        actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);
        assertTrue(listView.getItemsList()
                           .isEmpty());
    }

    @Test
    public void update_task_description_when_handled_event_task_description_updated() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(taskDescriptionUpdatedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);

        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertEquals(UPDATED_DESCRIPTION, taskView.getDescription());
    }

    @Test
    public void not_update_task_description_when_handled_event_task_description_updated_with_wrong_task_id() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        final TaskDescriptionUpdated taskDescriptionUpdated =
                taskDescriptionUpdatedInstance(TaskId.getDefaultInstance(), UPDATED_DESCRIPTION);
        taskDescriptionUpdatedEvent = createEvent(taskDescriptionUpdated, eventContext);
        eventBus.post(taskDescriptionUpdatedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertNotEquals(UPDATED_DESCRIPTION, taskView.getDescription());
    }

    @Test
    public void update_task_priority_when_handled_event_task_priority_updated() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(taskPriorityUpdatedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertEquals(UPDATED_TASK_PRIORITY, taskView.getPriority());
    }

    @Test
    public void not_update_task_priority_when_handled_event_task_priority_updated_with_wrong_task_id() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        final TaskPriorityUpdated taskPriorityUpdated = taskPriorityUpdatedInstance(TaskId.getDefaultInstance(),
                                                                                    UPDATED_TASK_PRIORITY);
        taskPriorityUpdatedEvent = createEvent(taskPriorityUpdated, eventContext);
        eventBus.post(taskPriorityUpdatedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertNotEquals(UPDATED_TASK_PRIORITY, taskView.getPriority());
    }

    @Test
    public void update_task_due_date_when_handled_event_task_due_date_updated() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(taskDueDateUpdatedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertEquals(UPDATED_TASK_DUE_DATE, taskView.getDueDate());
    }

    @Test
    public void update_task_completed_state_when_handled_event_task_completed() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(taskCompletedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertTrue(taskView.getCompleted());
    }

    @Test
    public void update_task_completed_state_when_handled_event_task_completed_with_wrong_task_id() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(taskCompletedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertTrue(taskView.getCompleted());
    }

    @Test
    public void update_task_completed_state_when_handled_event_task_reopened() {
        final int expectedListSize = 1;
        eventBus.post(labelAssignedToTaskEvent);
        eventBus.post(taskCompletedEvent);
        eventBus.post(taskReopenedEvent);
        final LabelledTasksView labelledTasksView = repository.load(LABEL_ID)
                                                              .getState();
        matchedExpectedValues(labelledTasksView);
        final TaskListView listView = labelledTasksView.getLabelledTasks();
        final int actualListSize = listView.getItemsCount();

        assertEquals(expectedListSize, actualListSize);

        final TaskView taskView = listView.getItems(0);

        assertFalse(taskView.getCompleted());
    }

    private static void matchedExpectedValues(TaskView taskView) {
        assertEquals(TASK_ID, taskView.getId());
        assertEquals(LABEL_ID, taskView.getLabelId());
    }

    private static void matchedExpectedValues(LabelledTasksView labelledTaskView) {
        assertEquals(LabelColorView.valueOf(LabelColor.BLUE), labelledTaskView.getLabelColor());
        assertEquals(LABEL_TITLE, labelledTaskView.getLabelTitle());
    }

    /*
     * Stub projection repository
     */
    private static class LabelledTasksViewProjectionRepository
            extends ProjectionRepository<TaskLabelId, LabelledTasksViewProjection, LabelledTasksView> {

        @Override
        protected TaskLabelId getEntityId(Message event, EventContext context) {
            return LABEL_ID;
        }

        protected LabelledTasksViewProjectionRepository(BoundedContext boundedContext) {
            super(boundedContext);
        }
    }
}
