package ru.mail.polis.open.project.statemachine.states;

import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.List;

public class WeatherChatState implements ChatState {

    public static final String WEATHER = "Weather";

    public WeatherChatState(List<String> cities) {
        // TODO: Draw User Interface displaying cities on buttons
    }

    @Override
    public void update(ChatStateMachine stateMachine, String command) {
        // TODO: Implement this
    }
}
