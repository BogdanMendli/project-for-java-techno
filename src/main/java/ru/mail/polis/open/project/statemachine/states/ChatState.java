package ru.mail.polis.open.project.statemachine.states;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface ChatState {

    void update(Message message);
}
