package ru.mail.polis.open.project.statemachine.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

import java.util.List;

public class NewsChatState implements ChatState {

    private ChatStateMachine stateMachine;

    public NewsChatState(ChatStateMachine stateMachine, Message message) {
        // TODO: Draw User Interface displaying cities on buttons
        this.stateMachine = stateMachine;

        if (message != null) {
            Bot.getInstance().sendMsg(
                message,
                "Введите город",
                false,
                stateMachine.getStatisticsProvider().getMostFrequent(4, UserSearchStatisticsProvider.StatisticsMode.NEWS)
            );
        }
    }

    @Override
    public void update(Message message) {

        if (message.getText().equals("/toMainMenu")) {
            stateMachine.setState(new MainMenuChatState(stateMachine, message));
            return;
        }

        // TODO: Implement this
    }
}
