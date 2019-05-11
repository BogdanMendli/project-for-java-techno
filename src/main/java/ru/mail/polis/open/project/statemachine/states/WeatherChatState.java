package ru.mail.polis.open.project.statemachine.states;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class WeatherChatState implements ChatState {

    private static final String urlBeforeCityName = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String urlAfterCityName = "&units=metric&appid=6fff53a641b9b9a799cfd6b079f5cd4e";

    public WeatherChatState(List<String> cities) {
        // TODO: Draw User Interface displaying cities on buttons
    }

    @Override
    public void update(ChatStateMachine stateMachine, String command) {

        if (command.equals("/toMainMenu")) {
            stateMachine.setState(new MainMenuChatState());
            return;
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(stateMachine.getChatId());

        try {
            URL url = new URL(urlBeforeCityName + command + urlAfterCityName);

            Scanner in = new Scanner((InputStream) url.getContent());
            StringBuilder result = new StringBuilder();
            while (in.hasNext()) {
                result.append(in.nextLine());
            }

            JSONObject object = new JSONObject(result);
            JSONObject main = object.getJSONObject("main");
            JSONArray getArray = object.getJSONArray("weather");

            sendMessage.setText("В городе: " + object.getString("name") + "\n" +
                "Температура: " + main.getDouble("temp") + "C" + "\n" +
                "Влажность: " + main.getDouble("humidity") + "%" + "\n" +
                "Осадки: " + getArray.getJSONObject(0).get("main") + "\n" +
                "http://openweathermap.org/img/w/" + getArray.getJSONObject(0).get("main") + ".png");
        } catch (MalformedURLException e) {
            sendMessage.setText("Города не найдено!");
        } catch (IOException e) {
            sendMessage.setText("Что-то пошло не так :(");
        } finally {
            try {
                stateMachine.getBot().execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
