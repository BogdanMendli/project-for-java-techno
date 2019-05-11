package ru.mail.polis.open.project.statemachine;

import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;

public class ChatStateMachine {

    private ChatState state;

    public ChatStateMachine() {

        state = new MainMenuChatState();
    }

    public void update(String command) {

        state.update(this, command);
    }

    public void setState(ChatState state) {
        this.state = state;
    }
}
