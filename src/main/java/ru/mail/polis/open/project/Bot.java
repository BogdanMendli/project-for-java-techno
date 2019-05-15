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
import ru.mail.polis.open.project.statemachine.states.ChatState;
import ru.mail.polis.open.project.statemachine.states.NewsChatState;
import ru.mail.polis.open.project.statemachine.states.WeatherChatState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Bot extends TelegramLongPollingBot {

    enum ButtonsMode {
        CHAT,
        MESSAGE
    }

    public static final String TO_MAIN_MENU_BUTTON = "/toMainMenu";
    public static final String WEATHER_BUTTON = "Weather";
    public static final String NEWS_BUTTON = "News";

    private static final String START_BUTTON = "/start";
    private static final String SETTINGS_BUTTON = "/settings";
    private static final String HELP_BUTTON = "/help";

    private static Bot instance = null;
    private static ExecutorService executorService = Executors.newFixedThreadPool(8);

    private final Map<Long, ChatStateMachine> chatStateMachineSet;

    private Bot(DefaultBotOptions botOptions) {
        super(botOptions);

        chatStateMachineSet = new HashMap<>();
    }

    synchronized static Bot createInstance(DefaultBotOptions botOptions) {
        if (instance != null) {
            throw new IllegalArgumentException("Bot is already created");
        }

        instance = new Bot(botOptions);
        return instance;
    }

    public synchronized static Bot getInstance() {
        return instance;
    }

    private void sendMsg(
        Message message,
        String text,
        boolean replyRequired
    ) {
        sendMsg(
            message,
            text,
            replyRequired,
            ButtonsMode.CHAT,
            List.of(
                START_BUTTON,
                HELP_BUTTON,
                SETTINGS_BUTTON,
                TO_MAIN_MENU_BUTTON
            )
        );
    }

    private void sendMsg(
        Message message,
        String text,
        boolean replyRequired,
        ButtonsMode buttonsMode,
        List<String> buttonsNames
    ) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        sendMessage.setChatId(message.getChatId().toString());

        if (replyRequired) {
            sendMessage.setReplyToMessageId(message.getMessageId());
        }
        sendMessage.setText(text);
        try {
            switch (buttonsMode) {
                case CHAT: {
                    setChatButtons(
                        sendMessage,
                        buttonsNames
                    );
                    break;
                }
                case MESSAGE: {
                    setMessageButtons(
                        sendMessage,
                        buttonsNames
                    );
                    break;
                }
            }

            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void onUpdateReceived(Update update) {

        executorService.submit(() -> {
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
                            case START_BUTTON : {
                                sendMsg(
                                    message,
                                    "Привет! Я бот Чижик, буду летать за нужной тебе информацией! \n"
                                        + "Выбирай, что тебе хочется узнать, а я пока приготовлюсь  к полёту.",
                                    false
                                );
                                break;
                            } case HELP_BUTTON : {
                                sendMsg(
                                    message,
                                    "Чтобы я мог помочь тебе узнать нужную информацию - введи /start.\n"
                                        + "Команда для настроек - /setting.\n"
                                        + "Чтобы вернуться в главное меню используй команду /toMainMenu",
                                    true
                                );
                                break;
                            } case SETTINGS_BUTTON : {
                                sendMsg(
                                    message,
                                    "Что будем настраивать?",
                                    true
                                );
                                break;
                            } default: {
                                List<String> buttonsName = new ArrayList<>();
                                String result = chatStateMachineSet
                                    .get(message.getChatId())
                                    .update(
                                        message.getText(),
                                        message.getChatId(),
                                        buttonsName
                                    );

                                sendMsg(
                                    message,
                                    result,
                                    true,
                                    ButtonsMode.MESSAGE,
                                    buttonsName
                                );
                            }
                        }
                    }
                } else if (update.hasCallbackQuery()) {

                    Message callbackMessage = update.getCallbackQuery().getMessage();
                    ChatStateMachine stateMachine = chatStateMachineSet.get(callbackMessage.getChatId());

                    switch (update.getCallbackQuery().getData()) {
                        case WEATHER_BUTTON: {
                            ChatState state = new WeatherChatState(stateMachine);
                            requestProcessing(
                                stateMachine,
                                callbackMessage,
                                state
                            );
                            break;
                        } case NEWS_BUTTON: {
                            ChatState state = new NewsChatState(stateMachine);
                            requestProcessing(
                                stateMachine,
                                callbackMessage,
                                state
                            );
                            break;
                        } default: {
                            List<String> buttonsName = new ArrayList<>();
                            String result = stateMachine.update(
                                update.getCallbackQuery().getData(),
                                callbackMessage.getChatId(),
                                buttonsName
                            );
                            sendMsg(callbackMessage, result, false, ButtonsMode.MESSAGE, buttonsName);
                        }
                    }
                }
            }
        );
    }

    private void requestProcessing(
        ChatStateMachine stateMachine,
        Message callbackMessage,
        ChatState state
    ) {
        stateMachine.setState(state);

        List<String> buttonsName = new ArrayList<>();
        String result = state.getInitialData(buttonsName);
        sendMsg(
            callbackMessage,
            result,
            false,
            ButtonsMode.MESSAGE, buttonsName
        );
    }


    private void setMessageButtons(SendMessage sendMessage, List<String> buttonsNames) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();

        for (String buttonsName : buttonsNames) {
            inlineButtons.add(List.of(
                new InlineKeyboardButton(buttonsName).setCallbackData(buttonsName))
            );
        }

        inlineKeyboardMarkup.setKeyboard(inlineButtons);
    }


    private void setChatButtons(SendMessage sendMessage, List<String> buttonsNames) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        for (String buttonName : buttonsNames) {
            keyboardFirstRow.add(new KeyboardButton(buttonName));
        }
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername() {
        return "Chizhik";
    }

    public String getBotToken() {
        return "857689855:AAGhd--tUolV7XhPMrm0qsJO7gMjiaArhls";
    }
}
