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

package io.spine.examples.todolist.view;

import io.spine.examples.todolist.UserIoTest;
import io.spine.examples.todolist.action.Action;
import io.spine.examples.todolist.action.NoOpAction;
import io.spine.examples.todolist.action.Shortcut;
import io.spine.examples.todolist.action.TransitionAction;
import io.spine.examples.todolist.action.TransitionAction.TransitionActionProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static io.spine.examples.todolist.action.TransitionAction.newProducer;
import static io.spine.examples.todolist.view.AbstractView.getBackShortcut;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Dmytro Grankin
 */
@DisplayName("AbstractView should")
class AbstractViewTest extends UserIoTest {

    private static final String ACTION_NAME = "action";
    private static final Shortcut SHORTCUT = new Shortcut("s");

    private final AbstractView view = new AView();

    @Test
    @DisplayName("create NoOpAction as a back action for a root view")
    void createNoOpBackAction() {
        view.setScreen(getScreen());
        final Action back = view.createBackAction(ACTION_NAME, SHORTCUT);

        assertEquals(ACTION_NAME, back.getName());
        assertEquals(SHORTCUT, back.getShortcut());
        assertThat(back, instanceOf(NoOpAction.class));
    }

    @Test
    @DisplayName("create usual back action for a child view")
    void createUsualBackAction() {
        final AbstractView childView = new AView();
        final ActionListView rootView = new ActionListView("root view");
        rootView.addAction(newProducer(ACTION_NAME, SHORTCUT, childView));

        addAnswer(SHORTCUT.getValue()); // Open child view.
        addAnswer(getBackShortcut().getValue()); // Back to the root view.
        addAnswer(getBackShortcut().getValue()); // Quit from the root view.

        getScreen().renderView(rootView);

        final TransitionAction back =
                (TransitionAction) childView.createBackAction(ACTION_NAME, SHORTCUT);

        assertEquals(ACTION_NAME, back.getName());
        assertEquals(SHORTCUT, back.getShortcut());
        assertSame(rootView, back.getDestination());
        assertSame(childView, back.getSource());
    }

    @Test
    @DisplayName("clear all actions")
    void clearAllActions() {
        view.addAction(newProducer(ACTION_NAME, SHORTCUT, view));
        view.addAction(newProducer(ACTION_NAME, new Shortcut("s2"), view));
        view.clearActions();
        assertTrue(view.getActions()
                       .isEmpty());
    }

    @Test
    @DisplayName("add an action")
    void addAction() {
        final Set<Action> actions = view.getActions();
        final int sizeBeforeAddition = actions.size();

        view.addAction(newProducer(ACTION_NAME, SHORTCUT, view));

        final int sizeAfterAddition = sizeBeforeAddition + 1;
        assertEquals(sizeAfterAddition, actions.size());
    }

    @Test
    @DisplayName("not add null action")
    void notAddNullAction() {
        final NullProducer producer = new NullProducer(ACTION_NAME, SHORTCUT, view);
        assertThrows(NullPointerException.class, () -> view.addAction(producer));
    }

    @Test
    @DisplayName("not add the action with the back shortcut")
    void notAddActionWithBackShortcut() {
        assertThrows(IllegalArgumentException.class,
                     () -> view.addAction(newProducer(ACTION_NAME, getBackShortcut(), view)));
    }

    @Test
    @DisplayName("not add the action with an occupied shortcut")
    void notAddActionWithOccupiedShortcut() {
        final TransitionActionProducer<ActionListView, View> firstActionProducer =
                newProducer(ACTION_NAME, SHORTCUT, view);
        view.addAction(firstActionProducer);

        final String secondActionName = firstActionProducer.getName() + " difference";
        assertThrows(IllegalArgumentException.class,
                     () -> view.addAction(newProducer(secondActionName, SHORTCUT, view)));
    }

    @Test
    @DisplayName("ask about action selection while the shortcut is invalid")
    void askActionSelection() {
        final String validAnswer = getBackShortcut().getValue();
        addAnswer("invalid answer");
        addAnswer(validAnswer);

        view.render(getScreen());

        assertAllAnswersWereGiven();
    }

    private static class AView extends AbstractView {

        private AView() {
            super("View title");
        }

        @Override
        public void renderBody() {
        }
    }

    private static class NullProducer extends TransitionActionProducer<AbstractView, View> {

        private NullProducer(String name, Shortcut shortcut, View destination) {
            super(name, shortcut, destination);
        }

        @SuppressWarnings("ReturnOfNull") // Purpose of this class.
        @Override
        public TransitionAction<AbstractView, View> create(AbstractView source) {
            return null;
        }
    }
}
