package ru.mail.polis.open.project.statemachine.states;

import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class WeatherChatState implements ChatState {

    private static final String URL_BEFORE_CITY_NAME = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String URL_AFTER_CITY_NAME = "&units=metric&appid=6fff53a641b9b9a799cfd6b079f5cd4e";
    private final ChatStateMachine stateMachine;

    public WeatherChatState(ChatStateMachine stateMachine, Message message) {

        this.stateMachine = stateMachine;

        if (message != null) {
            Bot.getInstance().sendMsg(
                message,
                "Введите город",
                false,
                stateMachine.getStatisticsProvider().getMostFrequent(
                    4,
                    UserSearchStatisticsProvider.StatisticsMode.WEATHER
                )
            );
        }
    }

    @Override
    public void update(Message message) {

        if (message.getText().equals("/toMainMenu")) {
            stateMachine.setState(new MainMenuChatState(stateMachine, message));
            return;
        }

        try {
            URL url = new URL(URL_BEFORE_CITY_NAME + message.getText() + URL_AFTER_CITY_NAME);

            Scanner in = new Scanner((InputStream) url.getContent());
            StringBuilder result = new StringBuilder();
            while (in.hasNext()) {
                result.append(in.nextLine());
            }

            JSONObject object = new JSONObject(result.toString());
            JSONObject main = object.getJSONObject("main");
            JSONArray getArray = object.getJSONArray("weather");

            stateMachine.getStatisticsProvider().onWeatherSearch(object.getString("name"));

            Bot.getInstance().sendMsg(
                message,
                "В городе: " + object.getString("name") + "\n" +
                    "Температура: " + main.getDouble("temp") + "C" + "\n" +
                    "Влажность: " + main.getDouble("humidity") + "%" + "\n" +
                    "Осадки: " + getArray.getJSONObject(0).get("main") + "\n" +
                    "http://openweathermap.org/img/w/" + getArray.getJSONObject(0).get("icon") + ".png",
                true,
                stateMachine.getStatisticsProvider().getMostFrequent(
                    4,
                    UserSearchStatisticsProvider.StatisticsMode.WEATHER
                )
            );
        } catch (MalformedURLException e) {
            Bot.getInstance().sendMsg(message, "Город не найден!", true);
        } catch (IOException e) {
            Bot.getInstance().sendMsg(message, "Что-то пошло не так :(", true);
        }
    }
}
