package ru.mail.polis.open.project.statemachine.states;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.mail.polis.open.project.Bot;
import ru.mail.polis.open.project.statemachine.ChatStateMachine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NewsChatState implements ChatState {

    private static final String urlBeforeCityName = "https://news.rambler.ru/rss/";
    private static final byte LIMIT = 10;

    private final File file = new File("News Requests");

    public NewsChatState(List<String> cities) {
        // TODO: Draw User Interface displaying cities on buttons
    }

    @Override
    public void update(ChatStateMachine stateMachine, Message message) {

        if (message.getText().equals("/toMainMenu")) {
            stateMachine.setState(new MainMenuChatState());
            return;
        }

        try (FileWriter fw = new FileWriter(file)) {
            URL url = new URL(urlBeforeCityName + message.getText());

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));
            StringBuilder info = new StringBuilder("");
            byte currentNews = 0;

            for (SyndEntry entry : feed.getEntries()) {
                info.append(
                    (entry.getTitle() != null
                        ? "  Topic #" + ++currentNews + " : " + entry.getTitle()
                        : "title : NULL") + "\n\n"
                        + (entry.getDescription() != null
                        ? entry.getDescription().getValue()
                        : "description : NULL") + "\n\n"
                        + "-----------------------------" + "\n\n"
                );

                if (currentNews == LIMIT) {
                    break;
                }
            }

            fw.write(info.toString());
            Bot.getInstance().sendMsg(message, info.toString());
        } catch (MalformedURLException e) {
            Bot.getInstance().sendMsg(message, "Город не найден!");
        } catch (FeedException | IOException e) {
            e.printStackTrace();
        }

        // TODO: Implement this
    }
}
