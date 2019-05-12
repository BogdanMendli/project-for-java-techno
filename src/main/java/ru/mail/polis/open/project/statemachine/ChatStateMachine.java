package ru.mail.polis.open.project.statemachine;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

public class ChatStateMachine {

    private ChatState state;
    private UserSearchStatisticsProvider statistics;

    public ChatStateMachine() {
        state = new MainMenuChatState(this, null);
        statistics = new UserSearchStatisticsProvider();
    }

    public void update(Message message) {

        state.update(message);
    }

    public void setState(ChatState state) {
        this.state = state;
    }

    public UserSearchStatisticsProvider getStatisticsProvider() {

        return statistics;
    }
}
