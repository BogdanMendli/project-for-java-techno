package ru.mail.polis.open.project.statemachine.states;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

public interface ChatState {

    void update(ChatStateMachine stateMachine, Message message);
}
