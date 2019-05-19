package ru.mail.polis.open.project.statemachine.states;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.ArrayList;

class MainMenuChatStateTest {

    private static ChatStateMachine chatStateMachine;
    private static MainMenuChatState mainMenuChatState;

    @BeforeAll
    static void createInstance() {
        chatStateMachine = new ChatStateMachine();
        mainMenuChatState = new MainMenuChatState(chatStateMachine);
    }

    @Test
    void testWorkingUpdate() {

        Assertions.assertEquals(
             "Ты уже в главном меню!",
            mainMenuChatState.update(
                "/menu",
                4455L,
                new ArrayList<>()
            )
        );
        Assertions.assertEquals(
            new MainMenuChatState(chatStateMachine),
            chatStateMachine.getState()
        );
        Assertions.assertThrows(
            NullPointerException.class,
            () -> chatStateMachine.update(
                null,
                564356L,
                new ArrayList<>())
        );
        Assertions.assertNull(
            mainMenuChatState.update(
            "Weather",
            34534L,
            new ArrayList<>()
            )
        );

        Assertions.assertEquals(
            new WeatherChatState(chatStateMachine),
            chatStateMachine.getState()
        );

        Assertions.assertNull(
            mainMenuChatState.update(
                "News",
                34534L,
                new ArrayList<>()
            )
        );

        Assertions.assertEquals(
            new NewsChatState(chatStateMachine),
            chatStateMachine.getState()
        );
    }

    @Test
    void testWorkingGetInitialData() {
        Assertions.assertEquals(
            "Ты в главном меню!",
            mainMenuChatState.getInitialData(new ArrayList<>())
        );
    }
}
