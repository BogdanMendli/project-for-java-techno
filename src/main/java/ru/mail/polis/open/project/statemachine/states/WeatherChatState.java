package ru.mail.polis.open.project.statemachine.states;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class WeatherChatState implements ChatState {

    private static final String URL_BEFORE_CITY_NAME = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String URL_AFTER_CITY_NAME = "&units=metric&appid=6fff53a641b9b9a799cfd6b079f5cd4e";
    private static final String URL_BEFORE_ICON = "http://openweathermap.org/img/w/";
    private final ChatStateMachine stateMachine;

    public WeatherChatState(ChatStateMachine stateMachine) {

        this.stateMachine = stateMachine;
    }

    @Override
    public String update(
        String message,
        long chatId,
        List<String> buttonsNames
    ) {
        if (message.equals(Bot.MENU_COMMAND)) {
            stateMachine.setState(new MainMenuChatState(stateMachine));
            return null;
        }

        try {
            URL url = new URL(URL_BEFORE_CITY_NAME + message + URL_AFTER_CITY_NAME);

            Scanner in = new Scanner((InputStream) url.getContent());
            StringBuilder result = new StringBuilder();
            while (in.hasNext()) {
                result.append(in.nextLine());
            }

            JSONObject object = new JSONObject(result.toString());
            JSONObject main = object.getJSONObject("main");
            JSONArray getArray = object.getJSONArray("weather");

            stateMachine.getStatisticsProvider().onWeatherSearch(object.getString("name"));

            buttonsNames.addAll(
                getMostFrequentCities()
            );

            UserSearchStatisticsProvider.addInfoAboutRequest(
                message,
                chatId,
                "Weather"
            );

            return "В городе: " + object.getString("name") + "\n" +
                "Температура: " + main.getDouble("temp") + "C" + "\n" +
                "Влажность: " + main.getDouble("humidity") + "%" + "\n" +
                "Осадки: " + getArray.getJSONObject(0).get("main") + "\n" +
                URL_BEFORE_ICON + getArray.getJSONObject(0).get("icon") + ".png";

        } catch (IOException e) {
            return "Город не найден!";
        }
    }

    @Override
    public String getInitialData(List<String> buttonsNames) {
        buttonsNames.addAll(
            getMostFrequentCities()
        );
        return "Погода\nВведите город на английском";
    }

    private List<String> getMostFrequentCities() {
        return stateMachine.getStatisticsProvider().getMostFrequent(
            4,
            UserSearchStatisticsProvider.StatisticsMode.WEATHER
        );
    }
}
