package ru.mail.polis.open.project.statistics;

import java.io.File;
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
    private static Map<Long, File> files;

    public UserSearchStatisticsProvider() {

        citiesNewsSearchCounter = new HashMap<>();
        citiesWeatherSearchCounter = new HashMap<>();
        files = new HashMap<>();
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
            if (!files.containsKey(chatId)) {
                files.put(chatId, new File("Statistic-" + chatId));
            }
            FileWriter fw = new FileWriter(files.get(chatId), true);

            fw.write(
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

    public synchronized static Map<Long, File> getCurrentStatistic() {
        return files;
    }

    public static synchronized void resetRequest(Long chatId) {
        try (FileWriter fw = new FileWriter(files.get(chatId))) {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        citiesWeatherSearchCounter.clear();
        citiesNewsSearchCounter.clear();
    }
}
