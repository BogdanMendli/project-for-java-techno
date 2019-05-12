package ru.mail.polis.open.project.statemachine.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.List;

public class MainMenuChatState implements ChatState {

    private final ChatStateMachine stateMachine;

    public MainMenuChatState(ChatStateMachine stateMachine, Message message) {
        this.stateMachine = stateMachine;

        if (message != null) {
            Bot.getInstance().sendMsg(
                message,
                "Вы в главном меню",
                false,
                List.of("Weather", "News")
            );
        }
    }

    @Override
    public void update(Message message) {

        switch (message.getText()) {
            case "/toMainMenu" : {
                Bot.getInstance().sendMsg(
                    message,
                    "Ты уже в главном меню!",
                    true,
                    List.of("Weather", "News")
                );
                break;
            } case "Weather" : {
                stateMachine.setState(new WeatherChatState(stateMachine, message));
                break;
            } case "News" : {
                stateMachine.setState(new NewsChatState(stateMachine, message));
                break;
            } default : {
                Bot.getInstance().sendMsg(
                    message,
                    "У меня нет такой функции (",
                    true
                );
            }
        }

        // TODO: Implement this
    }
}
