package ru.mail.polis.open.project.statemachine.states;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;
import ru.mail.polis.open.project.statistics.UserSearchStatisticsProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

public class NewsChatState implements ChatState {

    private static final String URL_BEFORE_CITY_NAME = "https://news.rambler.ru/";
    private static final String RSS = "rss/";
    private static final byte LIMIT = 5;

    private final File file = new File("News Requests");
    private ChatStateMachine stateMachine;

    public NewsChatState(ChatStateMachine stateMachine, Message message) {
        // TODO: Draw User Interface displaying cities on buttons
        this.stateMachine = stateMachine;

        if (message != null) {
            Bot.getInstance().sendMsg(
                message,
                "Введите город",
                false,
                stateMachine.getStatisticsProvider().getMostFrequent(
                    4,
                    UserSearchStatisticsProvider.StatisticsMode.NEWS
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

        try (FileWriter fw = new FileWriter(file)) {
            URL url = new URL(
                URL_BEFORE_CITY_NAME
                + RSS
                + message.getText()
            );

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            StringBuilder info = new StringBuilder("");
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
                    )
                    .append("\n\n---------------------------------------------------------\n\n");

                if (currentNews == LIMIT) {
                    break;
                }
            }

            info.append("Source : ")
                .append(URL_BEFORE_CITY_NAME)
                .append(message.getText());

            fw.write(
                message.getChatId().toString()
                + " : Request about News. City - "
                + message.getText() + " at "
                + LocalDateTime.now()
            );
            Bot.getInstance().sendMsg(message, info.toString(), true);
        } catch (MalformedURLException e) {
            Bot.getInstance().sendMsg(message, "Город не найден!", true);
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }

        // TODO: Implement this
    }
}
