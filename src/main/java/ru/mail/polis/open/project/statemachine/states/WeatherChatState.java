package ru.mail.polis.open.project.statemachine.states;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
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
    public void update(ChatStateMachine stateMachine, Message message) {

        if (message.getText().equals("/toMainMenu")) {
            stateMachine.setState(new MainMenuChatState());
            return;
        }

        try {
            URL url = new URL(urlBeforeCityName + message.getText() + urlAfterCityName);

            Scanner in = new Scanner((InputStream) url.getContent());
            StringBuilder result = new StringBuilder();
            while (in.hasNext()) {
                result.append(in.nextLine());
            }

            JSONObject object = new JSONObject(result.toString());
            JSONObject main = object.getJSONObject("main");
            JSONArray getArray = object.getJSONArray("weather");

            Bot.getInstance().sendMsg(
                message,
                "В городе: " + object.getString("name") + "\n" +
                    "Температура: " + main.getDouble("temp") + "C" + "\n" +
                    "Влажность: " + main.getDouble("humidity") + "%" + "\n" +
                    "Осадки: " + getArray.getJSONObject(0).get("main") + "\n" +
                    "http://openweathermap.org/img/w/" + getArray.getJSONObject(0).get("main") + ".png");
        } catch (MalformedURLException e) {
            Bot.getInstance().sendMsg(message, "Город не найден!");
        } catch (IOException e) {
            Bot.getInstance().sendMsg(message, "Что-то пошло не так :(");
        }
    }
}
