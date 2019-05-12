package ru.mail.polis.open.project;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statemachine.states.NewsChatState;
import ru.mail.polis.open.project.statemachine.states.WeatherChatState;

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

    public void sendMsg(Message message, String text, boolean replyRequired) {

        sendMsg(message, text, replyRequired, List.of());
    }

    public void sendMsg(Message message, String text, boolean replyRequired, List<String> buttonsNames) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());

        if (replyRequired) {
            sendMessage.setReplyToMessageId(message.getMessageId());
        }

        sendMessage.setText(text);
        try {
            setMessageButtons(sendMessage, buttonsNames);
            //setChatButtons(sendMessage);
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {

        // TODO: Call state machine
        if (update.hasMessage() && update.getMessage().hasText()) {
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
                                + "Выбирай, что тебе хочется узнать, а я пока приготовлюсь  к полёту.",
                            true
                        );
                        break;
                    }
                    case "/help": {
                        sendMsg(
                            message,
                            "Чтобы я мог помочь тебе узнать нужную информацию - введи /start. \n"
                                + "А для настроек есть команда /setting.",
                            true
                        );
                        break;
                    }
                    case "/setting": {
                        sendMsg(message, "Что будем настраивать?", true);
                        break;
                    }
                    default: {
                        chatStateMachineSet.get(message.getChatId()).update(message);
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            ChatStateMachine stateMachine = chatStateMachineSet.get(update.getCallbackQuery().getMessage().getChatId());
            switch (update.getCallbackQuery().getData()) {
                case "Weather" : {
                    stateMachine.setState(new WeatherChatState(stateMachine, update.getCallbackQuery().getMessage()));
                    break;
                }
                case "News" : {
                    stateMachine.setState(new NewsChatState(stateMachine, update.getCallbackQuery().getMessage()));
                    break;
                }
                default: {
                    stateMachine.update(update.getCallbackQuery().getMessage());
                }
            }
        }
    }


    private void setMessageButtons(SendMessage sendMessage, List<String> buttonsNames) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();

        for (String buttonsName : buttonsNames) {
            inlineButtons.add(List.of(new InlineKeyboardButton(buttonsName).setCallbackData(buttonsName)));
        }

        inlineKeyboardMarkup.setKeyboard(inlineButtons);
    }


    public void setChatButtons(SendMessage sendMessage) {

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
        keyboardFirstRow.add(new KeyboardButton("/toMainMenu"));

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
