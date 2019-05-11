package ru.mail.polis.open.project.statemachine;

import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;

public class ChatStateMachine {

    private long chatId;
    private Bot bot;
    private ChatState state;

    public ChatStateMachine(long chatId, Bot bot) {

        this.chatId = chatId;
        this.bot = bot;
        state = new MainMenuChatState();
    }

    public void update(String command) {

        state.update(this, command);
    }

    public void setState(ChatState state) {
        this.state = state;
    }

    public long getChatId() {
        return chatId;
    }

    public Bot getBot() {
        return bot;
    }
}
