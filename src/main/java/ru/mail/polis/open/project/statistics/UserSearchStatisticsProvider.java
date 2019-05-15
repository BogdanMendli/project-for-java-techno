package ru.mail.polis.open.project.statistics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserSearchStatisticsProvider {

    public enum StatisticsMode {
        WEATHER,
        NEWS
    }

    private final Map<String, Integer> citiesWeatherSearchCounter;
    private final Map<String, Integer> citiesNewsSearchCounter;
    private static FileWriter fw;
    private static File file;
    private static int countTakenStatistic;

    public UserSearchStatisticsProvider() {

        citiesNewsSearchCounter = new HashMap<>();
        citiesWeatherSearchCounter = new HashMap<>();
        countTakenStatistic = 0;
        try {
            file = new File(
                "Statistic-" + countTakenStatistic + ".txt");
            fw = new FileWriter(file, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onWeatherSearch(String city) {
        citiesWeatherSearchCounter.compute(
            city,
            (key, oldValue) -> oldValue == null ? 1 : oldValue + 1
        );
    }

    public void onNewsSearch(String city) {
        citiesNewsSearchCounter.compute(
            city,
            (key, oldValue) -> oldValue == null ? 1 : oldValue + 1
        );
    }

    public static void addInfoAboutRequest(String message, Long chatId, String opportunity) {
        LocalDateTime messageRequestTime = LocalDateTime.now();

        try {
            UserSearchStatisticsProvider.fw.write(
                "chatId : "
                    + chatId.toString()
                    + " : Request about " + opportunity + ". City - "
                    + message + " at "
                    + messageRequestTime.getHour() + ":"
                    + messageRequestTime.getMinute() + " "
                    + messageRequestTime.getDayOfMonth() + "-"
                    + messageRequestTime.getMonth().getValue() + "-"
                    + messageRequestTime.getYear()
                    + "\n"
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getMostFrequent(int count, StatisticsMode mode) {

        Map<String, Integer> requiredMap;
        switch (mode) {
            case WEATHER: {
                requiredMap = citiesWeatherSearchCounter;
                break;
            } case NEWS: {
                requiredMap = citiesNewsSearchCounter;
                break;
            } default: {
                throw new IllegalArgumentException("No such statistics mode");
            }
        }

        return requiredMap
            .entrySet()
            .stream()
            .sorted(((o1, o2) -> o2.getValue().compareTo(o1.getValue())))
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public static File getCurrentStatistic() {

        File currentFile = file;
        try {
            fw.close();
            BufferedReader br = new BufferedReader(new FileReader(currentFile));
            file = new File("Statistic-" + ++countTakenStatistic + ".txt");
            fw = new FileWriter(file);
            br.lines().forEach((line) -> {
                try {
                    fw.write(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentFile;
    }

    public void clear() {
        citiesWeatherSearchCounter.clear();
        citiesNewsSearchCounter.clear();
    }
}
