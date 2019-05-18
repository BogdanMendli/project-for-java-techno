package ru.mail.polis.open.project;

import org.junit.jupiter.api.Test;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;
import ru.mail.polis.open.project.statemachine.states.NewsChatState;
import ru.mail.polis.open.project.statemachine.states.WeatherChatState;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChatStateMachineTest {

    @Test
    void testWorkingUpdate() {
        ChatStateMachine chatStateMachine = new ChatStateMachine();
        assertEquals(
            chatStateMachine.getState(),
            new MainMenuChatState(chatStateMachine)
        );
        assertThrows(
            NullPointerException.class,
            () -> chatStateMachine.update(
                null,
                64565345L,
                new ArrayList<>()
            )
        );

        assertEquals(
            "Ты уже в главном меню!",
            chatStateMachine.update(
                "/menu",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new MainMenuChatState(chatStateMachine)
        );

        assertEquals(
            "Погода\nВведите город на английском",
            chatStateMachine.update(
                "Weather",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new WeatherChatState(chatStateMachine)
        );

        assertEquals(
            "Город не найден!",
            chatStateMachine.update(
                "Weather",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new WeatherChatState(chatStateMachine)
        );

        assertEquals(
            "Город не найден!",
            chatStateMachine.update(
                "News",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new WeatherChatState(chatStateMachine)
        );

        assertEquals(
            "Ты в главном меню!",
            chatStateMachine.update(
                "/menu",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new MainMenuChatState(chatStateMachine)
        );

        assertEquals(
            "Новости\nВведите город на английском",
            chatStateMachine.update(
                "News",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new NewsChatState(chatStateMachine)
        );

        assertEquals(
            "Город не найден!",
            chatStateMachine.update(
                "Weather",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new NewsChatState(chatStateMachine)
        );

        assertEquals(
            "Город не найден!",
            chatStateMachine.update(
                "News",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new NewsChatState(chatStateMachine)
        );

        assertEquals(
            "Ты в главном меню!",
            chatStateMachine.update(
                "/menu",
                545235L,
                new ArrayList<>()
            )
        );
        assertEquals(
            chatStateMachine.getState(),
            new MainMenuChatState(chatStateMachine)
        );
    }
}
