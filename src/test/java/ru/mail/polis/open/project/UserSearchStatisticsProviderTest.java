package ru.mail.polis.open.project;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserSearchStatisticsProviderTest {

    private static UserSearchStatisticsProvider statisticsProvider = new UserSearchStatisticsProvider();

    @Test
    void testWorkingOnWeatherSearch() {
        statisticsProvider.onWeatherSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 1);
        statisticsProvider.onWeatherSearch("Luga");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 2);
        statisticsProvider.onWeatherSearch("Surgut");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 3);
        statisticsProvider.onWeatherSearch("Simpheropol");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 4);
        statisticsProvider.onWeatherSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 4);
        statisticsProvider.onWeatherSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 4);
        statisticsProvider.onWeatherSearch("Bryansk");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 5);
        statisticsProvider.onWeatherSearch("Pskov");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 6);
        statisticsProvider.onWeatherSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 6);
        statisticsProvider.onWeatherSearch("Luga");
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 6);

        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().get("Moscow"), 4);
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().get("Luga"), 2);
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().get("Surgut"), 1);
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().get("Simpheropol"), 1);
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().get("Bryansk"), 1);
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().get("Pskov"), 1);
        assertEquals(statisticsProvider.getCitiesWeatherSearchCounter().size(), 6);
    }

    @Test
    void testWorkingOnNewsSearch() {
        statisticsProvider.onNewsSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 1);
        statisticsProvider.onNewsSearch("Luga");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 2);
        statisticsProvider.onNewsSearch("Surgut");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 3);
        statisticsProvider.onNewsSearch("Simpheropol");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 4);
        statisticsProvider.onNewsSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 4);
        statisticsProvider.onNewsSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 4);
        statisticsProvider.onNewsSearch("Bryansk");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 5);
        statisticsProvider.onNewsSearch("Pskov");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 6);
        statisticsProvider.onNewsSearch("Moscow");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 6);
        statisticsProvider.onNewsSearch("Luga");
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 6);

        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().get("Moscow"), 4);
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().get("Luga"), 2);
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().get("Surgut"), 1);
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().get("Simpheropol"), 1);
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().get("Bryansk"), 1);
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().get("Pskov"), 1);
        assertEquals(statisticsProvider.getCitiesNewsSearchCounter().size(), 6);
    }

    @Test
    void testWorkingGetMostFrequent() {
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Surgut");
        statisticsProvider.onNewsSearch("Simpheropol");
        statisticsProvider.onNewsSearch("Simpheropol");
        statisticsProvider.onNewsSearch("Simpheropol");
        statisticsProvider.onNewsSearch("Simpheropol");
        statisticsProvider.onNewsSearch("Simpheropol");
        statisticsProvider.onNewsSearch("Simpheropol");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Bryansk");
        statisticsProvider.onNewsSearch("Bryansk");
        statisticsProvider.onNewsSearch("Pskov");
        statisticsProvider.onNewsSearch("Pskov");
        statisticsProvider.onNewsSearch("Pskov");
        statisticsProvider.onNewsSearch("Pskov");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");

        assertEquals(
            statisticsProvider.getMostFrequent(
                2,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).size(),
            2
        );

        assertTrue(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).contains("Luga")
        );
        assertTrue(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).contains("Moscow")
        );
        assertTrue(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).contains("Simpheropol")
        );
        assertFalse(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).contains("Pskov")
        );
        assertFalse(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).contains("Bryansk")
        );
        assertFalse(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.NEWS
            ).contains("Surgut")
        );

        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Surgut");
        statisticsProvider.onWeatherSearch("Simpheropol");
        statisticsProvider.onWeatherSearch("Simpheropol");
        statisticsProvider.onWeatherSearch("Simpheropol");
        statisticsProvider.onWeatherSearch("Simpheropol");
        statisticsProvider.onWeatherSearch("Simpheropol");
        statisticsProvider.onWeatherSearch("Simpheropol");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Bryansk");
        statisticsProvider.onWeatherSearch("Bryansk");
        statisticsProvider.onWeatherSearch("Pskov");
        statisticsProvider.onWeatherSearch("Pskov");
        statisticsProvider.onWeatherSearch("Pskov");
        statisticsProvider.onWeatherSearch("Pskov");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Luga");

        assertEquals(
            statisticsProvider.getMostFrequent(
                2,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).size(),
            2
        );

        assertTrue(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).contains("Luga")
        );
        assertTrue(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).contains("Moscow")
        );
        assertTrue(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).contains("Simpheropol")
        );
        assertFalse(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).contains("Pskov")
        );
        assertFalse(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).contains("Bryansk")
        );
        assertFalse(
            statisticsProvider.getMostFrequent(
                3,
                UserSearchStatisticsProvider.BotAbility.WEATHER
            ).contains("Surgut")
        );
    }

    @Test
    void testWorkingAddInfoAboutRequest() {
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Moscow",
            444563L,
            "News"
        );
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Moscow",
            444563L,
            "Weather"
        );
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Bryansk",
            66757L,
            "News"
        );
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Luga",
            44563565463L,
            "News"
        );
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Moscow",
            456463453563L,
            "Weather"
        );

        assertEquals(
            UserSearchStatisticsProvider.getCurrentStatistics().size(),
            4
        );
        assertTrue(UserSearchStatisticsProvider.getCurrentStatistics().containsKey(444563L));
        assertTrue(UserSearchStatisticsProvider.getCurrentStatistics().containsKey(66757L));
        assertTrue(UserSearchStatisticsProvider.getCurrentStatistics().containsKey(44563565463L));
        assertTrue(UserSearchStatisticsProvider.getCurrentStatistics().containsKey(456463453563L));
        assertEquals(
            UserSearchStatisticsProvider.getCurrentStatistics().get(444563L).getName(),
            "Statistic-" + 444563L + ".txt"
        );
        assertEquals(
            UserSearchStatisticsProvider.getCurrentStatistics().get(66757L).getName(),
            "Statistic-" + 66757L + ".txt"
        );
        assertEquals(
            UserSearchStatisticsProvider.getCurrentStatistics().get(44563565463L).getName(),
            "Statistic-" + 44563565463L + ".txt"
        );
        assertEquals(
            UserSearchStatisticsProvider.getCurrentStatistics().get(456463453563L).getName(),
            "Statistic-" + 456463453563L + ".txt"
        );
    }

    @Test
    void testWorkingClear() {
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Moscow");
        statisticsProvider.onWeatherSearch("Luga");
        statisticsProvider.onWeatherSearch("Luga");

        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Moscow");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");
        statisticsProvider.onNewsSearch("Luga");

        assertEquals(
            statisticsProvider.getCitiesWeatherSearchCounter().size(),
            2
        );
        assertEquals(
            statisticsProvider.getCitiesNewsSearchCounter().size(),
            2
        );

        statisticsProvider.clear();

        assertEquals(
            statisticsProvider.getCitiesWeatherSearchCounter().size(),
            0
        );
        assertEquals(
            statisticsProvider.getCitiesNewsSearchCounter().size(),
            0
        );
    }

    @Test
    void testResetRequest() {
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Moscow",
            444563L,
            "News"
        );UserSearchStatisticsProvider.addInfoAboutRequest(
            "Moscow",
            444563L,
            "Weather"
        );
        UserSearchStatisticsProvider.addInfoAboutRequest(
            "Moscow",
            444563L,
            "Weather"
        );

        try {
            BufferedReader br = new BufferedReader(
                new FileReader(UserSearchStatisticsProvider.getCurrentStatistics().get(444563L))
            );
            assertEquals(br.lines().count(), 3);
            statisticsProvider.clear(444563L);
            assertEquals(br.lines().count(), 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void reset() {
        statisticsProvider.getCitiesNewsSearchCounter().clear();
        statisticsProvider.getCitiesWeatherSearchCounter().clear();
        UserSearchStatisticsProvider.getCurrentStatistics().clear();
        UserSearchStatisticsProvider.clearAllRequest();
    }
}
