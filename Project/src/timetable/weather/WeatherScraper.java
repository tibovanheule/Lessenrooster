//Tibo Vanheule
package timetable.weather;

import org.json.JSONObject;
import timetable.config.Config;
import timetable.objects.Weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class WeatherScraper {
    private Weather weather = null;

    private String getData() {
        //stringbuilder zodat we later de inputline er aan toe kunenn voegen (sneller dan string concatenatie += )
        StringBuilder string = new StringBuilder();
        try {
            Config config = new Config();
            Properties properties = config.getproperties();
            String key = properties.getProperty("weather.key");
            String city = properties.getProperty("weather.city");
            URL weather = new URL("http://api.wunderground.com/api/" + key + "/conditions/q/be/" + city + ".json");
            String inputLine;
            BufferedReader input = new BufferedReader(new InputStreamReader(weather.openStream()));
            while ((inputLine = input.readLine()) != null) {
                string.append(inputLine);
            }
        } catch (Exception e) {
            weather = new Weather(false);
            e.printStackTrace();
        }
        return string.toString();
    }

    public Weather getWeather() {
        try {
            String jsonString = getData();
            //deze if voor als het ophalen niet gelukt is dan wordt er geen extra error opgegooid
            if (weather == null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONObject observation = jsonObject.getJSONObject("current_observation");
                Double degree = observation.getDouble("temp_c");
                Double windDegree = observation.getDouble("wind_degrees");
                Double windSpeed = observation.getDouble("wind_kph");
                String humidity = observation.getString("relative_humidity");
                String icon = observation.getString("icon");
                String condition = observation.getString("weather");
                JSONObject location = observation.getJSONObject("display_location");
                String city = location.getString("city");
                weather = new Weather(true, degree, windDegree, windSpeed, humidity, icon, condition, city);
            }
        } catch (Exception e) {
            weather = new Weather(false);
            e.printStackTrace();
        }
        return weather;
    }
}
