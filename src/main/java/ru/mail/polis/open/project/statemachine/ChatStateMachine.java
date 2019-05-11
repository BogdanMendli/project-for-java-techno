package ru.mail.polis.open.project.statemachine;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;

public class ChatStateMachine {

    private Bot bot;
    private ChatState state;

    public ChatStateMachine(Bot bot) {

        this.bot = bot;
        state = new MainMenuChatState();
    }

    public void update(Message message) {

        state.update(this, message);
    }

    public void setState(ChatState state) {
        this.state = state;
    }

    public Bot getBot() {
        return bot;
    }
}
