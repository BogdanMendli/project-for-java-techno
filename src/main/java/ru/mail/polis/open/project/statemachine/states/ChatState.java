package ru.mail.polis.open.project.statemachine.states;

import ru.mail.polis.open.project.statemachine.ChatStateMachine;

public interface ChatState {

    void update(ChatStateMachine stateMachine, String command);
}
