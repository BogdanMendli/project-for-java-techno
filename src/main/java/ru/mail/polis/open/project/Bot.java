package ru.mail.polis.open.project;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bot extends TelegramLongPollingBot {

    private final Map<Long, ChatStateMachine> chatStateMachineSet;

    private static Bot instance = null;

    protected Bot(DefaultBotOptions botOptions) {
        super(botOptions);

        chatStateMachineSet = new HashMap<>();
    }

    public synchronized static Bot createInstance(DefaultBotOptions botOptions) {
        if (instance != null) {
            throw new IllegalArgumentException("Bot is already created");
        }

        instance = new Bot(botOptions);
        return instance;
    }

    public synchronized static Bot getInstance() {
        return instance;
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());

        sendMessage.setReplyToMessageId(message.getMessageId());

        sendMessage.setText(text);
        try {

            setButtons(sendMessage);
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {

        // TODO: Call state machine

        Message message = update.getMessage();

        if (message == null) {
            return;
        }

        if (!chatStateMachineSet.containsKey(message.getChatId())) {
            chatStateMachineSet.put(
                message.getChatId(),
                new ChatStateMachine()
            );
        }

        if (message.hasText()) {
            switch (message.getText()) {
                case "/start": {
                    sendMsg(
                        message,
                        "Привет! Я бот Чижик, буду летать за нужной тебе информацией! \n"
                            + "Выбирай, что тебе хочется узнать, а я пока приготовлюсь  к полёту."
                    );
                    break;
                }
                case "/help": {
                    sendMsg(
                        message,
                        "Чтобы я мог помочь тебе узнать нужную информацию - введи /start. \n"
                            + "А для настроек есть команда /setting."
                    );
                }
                case "/setting": {
                    sendMsg(message, "Что будем настраивать?");
                    break;
                }
                default: {
                    chatStateMachineSet.get(message.getChatId()).update(message);
                }
            }
        }
    }


    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/start"));
        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    public String getBotUsername() {
        return "Chizhik";
    }

    public String getBotToken() {
        return "857689855:AAGhd--tUolV7XhPMrm0qsJO7gMjiaArhls";
    }
}
