package ru.mail.polis.open.project.statemachine.states;

import java.util.List;

public interface ChatState {

    String update(String message, long chatId, List<String> buttonsNames);

    String getInitialData(List<String> buttonsNames);
}
