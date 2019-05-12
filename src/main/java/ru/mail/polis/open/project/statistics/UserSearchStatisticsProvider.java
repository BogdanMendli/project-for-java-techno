package ru.mail.polis.open.project.statistics;

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

    public UserSearchStatisticsProvider() {

        citiesNewsSearchCounter = new HashMap<>();
        citiesWeatherSearchCounter = new HashMap<>();
    }

    public void onWeatherSearch(String city) {
        citiesWeatherSearchCounter.compute(city, (key, oldValue) -> oldValue == null ? 1 : oldValue++);
    }

    public void onNewsSearch(String city) {
        citiesNewsSearchCounter.compute(city, (key, oldValue) -> oldValue == null ? 1 : oldValue++);
    }

    public List<String> getMostFrequent(int count, StatisticsMode mode) {

        Map<String, Integer> requiredMap;

        switch (mode) {
            case WEATHER: {
                requiredMap = citiesWeatherSearchCounter;
                break;
            }
            case NEWS: {
                requiredMap = citiesNewsSearchCounter;
                break;
            }
            default: throw new IllegalArgumentException("No such statistics mode");
        }

        return requiredMap
            .entrySet()
            .stream()
            .sorted()
            .limit(count)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public void clear() {
        citiesWeatherSearchCounter.clear();
        citiesNewsSearchCounter.clear();
    }
}
