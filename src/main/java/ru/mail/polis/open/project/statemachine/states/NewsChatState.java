package ru.mail.polis.open.project.statemachine.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.List;

public class NewsChatState implements ChatState {

    public NewsChatState(List<String> cities) {
        // TODO: Draw User Interface displaying cities on buttons
    }

    @Override
    public void update(ChatStateMachine stateMachine, Message message) {

        if (message.getText().equals("/toMainMenu")) {
            stateMachine.setState(new MainMenuChatState());
            return;
        }

        // TODO: Implement this
    }
}
