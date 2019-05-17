package ru.mail.polis.open.project.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Keeps statistics about users requests and writes it to files
 */
public class UserSearchStatisticsProvider {

    /**
     * Enumeration representing about what the request was made
     */
    public enum BotAbility {
        WEATHER,
        NEWS
    }

    private final Map<String, Integer> citiesWeatherSearchCounter;
    private final Map<String, Integer> citiesNewsSearchCounter;


    public UserSearchStatisticsProvider() {

        citiesNewsSearchCounter = new HashMap<>();
        citiesWeatherSearchCounter = new HashMap<>();
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

    /**
     * Logs chat history to file
     * @param message - request
     * @param chatId - where thee request was made
     * @param ability - which of the bots ability was used
     */
    public static void addInfoAboutRequest(String message, Long chatId, String ability) {
        LocalDateTime messageRequestTime = LocalDateTime.now();

        try (FileWriter fw = new FileWriter("logs/Statistic-" + chatId + ".txt", true)) {
            fw.write(
                "chatId : "
                    + chatId.toString()
                    + " : Request about " + ability + ". City - "
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

    /**
     * Returns most frequent request from users on given ability
     * @param count - how much items have to be returned
     * @param ability - statistics pool to return from
     * @return list of strings - most frequent requests
     */
    public List<String> getMostFrequent(int count, BotAbility ability) {

        Map<String, Integer> requiredMap;
        switch (ability) {
            case WEATHER: {
                requiredMap = citiesWeatherSearchCounter;
                break;
            } case NEWS: {
                requiredMap = citiesNewsSearchCounter;
                break;
            } default: {
                throw new IllegalArgumentException("No such statistics ability");
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

    /**
     * Clears all info about chat
     */
    public void clear(Long chatId) {
        citiesWeatherSearchCounter.clear();
        citiesNewsSearchCounter.clear();

        try (FileWriter fw = new FileWriter(new File("logs/Statistic-" + chatId + ".txt"))) {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getCitiesWeatherSearchCounter() {
        return citiesWeatherSearchCounter;
    }

    public Map<String, Integer> getCitiesNewsSearchCounter() {
        return citiesNewsSearchCounter;
    }

    public static synchronized void clearAllRequest() {
        for (File file : new File("logs").listFiles()) {
            try (FileWriter fw = new FileWriter(file)) {
                fw.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
