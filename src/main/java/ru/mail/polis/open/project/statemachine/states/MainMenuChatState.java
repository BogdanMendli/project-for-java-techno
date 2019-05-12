package ru.mail.polis.open.project.statemachine.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.ArrayList;

public class MainMenuChatState implements ChatState {

    public MainMenuChatState() {
        // TODO: Draw User Interface for it
    }

    @Override
    public void update(ChatStateMachine stateMachine, Message message) {

        switch (message.getText()) {
            case "/toMainMenu" : {
                Bot.getInstance().sendMsg(
                    message,
                    "Ты уже в главном меню!"
                );
                break;
            } case "Weather" : {
                Bot.getInstance().sendMsg(message, "В каком городе смотрим погоду?");
                stateMachine.setState(new WeatherChatState(new ArrayList<>()));
                break;
            } case "News" : {
                Bot.getInstance().sendMsg(message, "В каком городе смотрим новости?");
                stateMachine.setState(new NewsChatState(new ArrayList<>()));
                break;
            } default : {

            }
        }

        // TODO: Implement this
    }
}
