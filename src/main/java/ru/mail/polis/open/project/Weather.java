package ru.mail.polis.open.project;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Weather {
    static Scanner in;

    public static String getWeather(String message, Model model) throws IOException, InterruptedException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=6fff53a641b9b9a799cfd6b079f5cd4e");

//        Thread thread = new Thread(
//            ()->{
//                try {
//                    in = new Scanner((InputStream) url.getContent());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        );
//        thread.start();
//        thread.join();
        in = new Scanner((InputStream) url.getContent());

        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "В городе: " + model.getName() + "\n" +
            "Температура: " + model.getTemp() + "C" + "\n" +
            "Влажность: " + model.getHumidity() + "%" + "\n" +
            "Осадки: " + model.getMain() + "\n" +
            "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
