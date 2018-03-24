//Tibo Vanheule
package timetable.weather;

import org.json.JSONArray;
import org.json.JSONObject;
import timetable.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class Weather {
    private String getData(){
        String data = "";
        try{
            Config config = new Config();
            Properties properties =  config.getproperties();

            URL weather = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                    + properties.getProperty("weather.city")
                    + "," + properties.getProperty("weather.country")
                    + "&appid=" + properties.getProperty("weather.appid")
            );
            String inputLine;
            BufferedReader invoer = new BufferedReader(new InputStreamReader(weather.openStream()));
            while ((inputLine = invoer.readLine()) != null){
                data += inputLine;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return data;
    }
    public String[] getWeather(){
        System.out.println(getData());
        String jsonString = getData();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject weather = jsonObject.getJSONObject("main");
        System.out.println(weather.get("temp"));
        System.out.println();
        System.out.println(weather);
        //System.out.println(newJSON.toString());
        //jsonObject = new JSONObject(newJSON.toString());
        //System.out.println(jsonObject.getString("rcv"));
        //System.out.println(jsonObject.getJSONArray("argv"));
        return null;
    }
}
