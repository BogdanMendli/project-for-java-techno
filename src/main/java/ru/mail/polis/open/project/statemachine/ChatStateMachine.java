package ru.mail.polis.open.project.statemachine;

import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.MainMenuChatState;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

import java.util.List;

public class ChatStateMachine {

    private ChatState state;
    private UserSearchStatisticsProvider statistics;

    public ChatStateMachine() {

        state = new MainMenuChatState(this);
        statistics = new UserSearchStatisticsProvider();
    }

    public String update(
        String message,
        Long chatId,
        List<String> buttonsName
    ) {
        String result = state.update(message, chatId, buttonsName);

        if (result == null) {
            result = state.getInitialData(buttonsName);
        }

        return result;
    }

    public void setState(ChatState state) {
        this.state = state;
    }

    public UserSearchStatisticsProvider getStatisticsProvider() {

        return statistics;
    }
}
