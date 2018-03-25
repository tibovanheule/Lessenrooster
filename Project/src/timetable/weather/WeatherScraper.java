//Tibo Vanheule
package timetable.weather;

import org.json.JSONObject;
import timetable.config.Config;
import timetable.objects.Weather;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.util.Properties;

public class WeatherScraper {
    private String getData(){
        String data = "";
        try{
            Config config = new Config();
            Properties properties =  config.getproperties();
            String key = properties.getProperty("weather.key");
            String city = properties.getProperty("weather.city");
            URL weather = new URL("http://api.wunderground.com/api/"+key+"/conditions/q/be/"+city+".json");
            String inputLine;
            BufferedReader input = new BufferedReader(new InputStreamReader(weather.openStream()));
            while ((inputLine = input.readLine()) != null){
                data += inputLine;
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return data;
    }
    public Weather getWeather(){
        Weather weather = null;
        try {
            String jsonString = getData();
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject observation = jsonObject.getJSONObject("current_observation");
            Double degree = observation.getDouble("temp_c");
            Double windDegree = observation.getDouble("wind_degrees");
            Double windSpeed = observation.getDouble("wind_kph");
            String humidity = observation.getString("relative_humidity");
            String icon = observation.getString("icon");

            weather = new Weather(true,degree, windDegree, windSpeed, humidity, icon);
        } catch (Exception e) {
            weather = new Weather(false);
        }
        return weather;
    }
}
