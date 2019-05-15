package ru.mail.polis.open.project.statemachine.states;

import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.List;

public class MainMenuChatState implements ChatState {

    private final ChatStateMachine stateMachine;

    public MainMenuChatState(ChatStateMachine stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public String update(String message, long chatId, List<String> buttonsNames) {

        switch (message) {
            case "/toMainMenu" : {
                buttonsNames.addAll(getButtonsNames());
                return "Ты уже в главном меню!";
            } case "Weather" : {
                stateMachine.setState(new WeatherChatState(stateMachine));
                return null;
            } case "News" : {
                stateMachine.setState(new NewsChatState(stateMachine));
                return null;
            } default : {
                return "У меня нет такой функции (";
            }
        }
    }

    @Override
    public String getInitialData(List<String> buttonsNames) {
        buttonsNames.addAll(getButtonsNames());
        return "Вы в главном меню";
    }

    private List<String> getButtonsNames() {
        return List.of("Weather", "News");
    }
}
