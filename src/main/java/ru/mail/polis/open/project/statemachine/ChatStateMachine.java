package ru.mail.polis.open.project.statemachine;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;

public class ChatStateMachine {

    private ChatState state;

    public ChatStateMachine() {
        state = new MainMenuChatState();
    }

    public void update(Message message) {

        state.update(this, message);
    }

    public void setState(ChatState state) {
        this.state = state;
    }
}
