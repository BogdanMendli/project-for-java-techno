package ru.mail.polis.open.project.statemachine.states;

import ru.mail.polis.open.project.statemachine.ChatStateMachine;

public class MainMenuChatState implements ChatState {

    public static final String START = "/start";
    public static final String HELP = "/help";
    public static final String SETTING = "/setting";

    public MainMenuChatState() {
        // TODO: Draw User Interface for it
    }

    @Override
    public void update(ChatStateMachine stateMachine, String command) {
        // TODO: Implement this
    }
}
