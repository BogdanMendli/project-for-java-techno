package ru.mail.polis.open.project;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statemachine.states.NewsChatState;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewsChatStateTest {

    private static NewsChatState newsChatState;

    @BeforeAll
    void createInstance() {
        newsChatState = new NewsChatState(new ChatStateMachine());
    }

    @Test
    void testWorkingUpdate() throws IOException, FeedException {

        assertEquals(
            newsChatState.update(
                "gshshsth",
                345345L,
                new ArrayList<>()
            ),
            "Город не найден!"
        );

        assertNull(newsChatState.update("/menu", 453453L, new ArrayList<>()));

        URL url = new URL("https://news.rambler.ru/rss/Luga");

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(url));
        StringBuilder info = new StringBuilder();
        byte currentNews = 0;

        for (SyndEntry entry : feed.getEntries()) {
            info.append(
                (entry.getTitle() != null
                    ? "  Topic #" + ++currentNews + " : " + entry.getTitle()
                    : "title : NULL")
            )
                .append("\n\n")
                .append(
                    entry.getDescription() != null
                        ? entry.getDescription().getValue()
                        : "description : NULL"
                ).append("\n\n---------------------------------------------------------\n\n");

            if (currentNews == 5) {
                break;
            }
        }

        info.append("Source : ")
            .append("https://news.rambler.ru/")
            .append("Luga");

        assertEquals(
            info.toString(),
            newsChatState.update(
                "Luga",
                4353543L,
                new ArrayList<>()
            )
        );
    }

    @Test
    void testWorkingGetInitialData() {
        assertEquals(
            newsChatState.getInitialData(new ArrayList<>()),
            "Новости\nВведите город на английском"
        );
    }
}
