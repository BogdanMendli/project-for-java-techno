package ru.mail.polis.open.project;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;
import ru.mail.polis.open.project.statemachine.states.NewsChatState;
import ru.mail.polis.open.project.statemachine.states.WeatherChatState;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MainMenuChatStateTest {

    private static ChatStateMachine chatStateMachine;
    private static MainMenuChatState mainMenuChatState;

    @BeforeAll
    void createInstance() {
        chatStateMachine = new ChatStateMachine();
        mainMenuChatState = new MainMenuChatState(chatStateMachine);
    }

    @Test
    void testWorkingUpdate() {

        assertEquals(
            mainMenuChatState.update(
                "/menu",
                4455L,
                new ArrayList<>()
            ), "Ты уже в главном меню!"
        );
        assertEquals(
            chatStateMachine.getState(),
            new MainMenuChatState(chatStateMachine)
        );

        assertNull(
            mainMenuChatState.update(
            "Weather",
            34534L,
            new ArrayList<>()
            )
        );

        assertEquals(
            chatStateMachine.getState(),
            new WeatherChatState(chatStateMachine)
        );

        assertNull(
            mainMenuChatState.update(
                "News",
                34534L,
                new ArrayList<>()
            )
        );

        assertEquals(
            chatStateMachine.getState(),
            new NewsChatState(chatStateMachine)
        );
    }

    @Test
    void testWorkingGetInitialData() {
        assertEquals(
            mainMenuChatState.getInitialData(new ArrayList<>()),
            "Вы в главном меню"
        );
    }
}
