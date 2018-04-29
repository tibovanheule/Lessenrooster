//Tibo Vanheule
package timetable.weather;

import timetable.StdError;
import timetable.config.Config;
import timetable.objects.Weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Class to get weather data of a city (specified in the settings)
 */
public class WeatherScraper {
    private Weather weather = null;
    /*
     * Normaal Zou ik een JSON Library gebruikt hebben en rechtsteeks met de api gewerkt hebben. Deze is ook al geïmplementeerd (zie beneden, jar die gebruikt ik zou hebben ook toegevoegd in zip),
     * Maar om een extra library (Voor de JSON) te vermijden.
     * Laat ik nu mijn web server het werk doen. ik geef aan de url de authenticatie sleutel van api mee en ook de Stadsnaam (via URL parameters)
     * en krijg een de correcte output op aparte lijnen terug. daarmee kunnen we dat in array van strings maken.
     * Het formaat:
     *   --Icon name
     *   --Weer condition
     *   --Tempuratuur
     *   --Wind graden
     *   --Wind snelheid
     *   --Luchtvochtigheid
     *   --City
     */

    /**
     * Function to get the weather data
     */
    private ArrayList<String> getData() {
        ArrayList<String> strings = new ArrayList<>();
        try {
            Config config = new Config();
            Properties properties = config.getproperties();
            String key = properties.getProperty("weather.key");
            String city = properties.getProperty("weather.city");
            URL weather = new URL("https://tibovanheule.space/artifacts/wundergroundApi.php?key=" + key + "&city=" + city);
            String inputLine;
            BufferedReader input = new BufferedReader(new InputStreamReader(weather.openStream()));
            while ((inputLine = input.readLine()) != null) {
                strings.add(inputLine);
            }
        } catch (Exception e) {
            weather = new Weather(false);
            /*e.printStackTrace();*/
            new StdError("WARNING! couldn't get the weather! Do you have an internet connection?");

        }
        return strings;
    }


    /**
     * Function to make a Weather object
     */
    public Weather getWeather() {
        try {
            ArrayList<String> jsonString = getData();
            //deze if voor als het ophalen niet gelukt is dan wordt er geen extra error opgegooid
            if (weather == null) {
                /*
                 * Hoe ik het normaal zou gedaan hebben
                 * JSONObject jsonObject = new JSONObject(jsonString);
                 * JSONObject observation = jsonObject.getJSONObject("current_observation");
                 * Double degree = observation.getDouble("temp_c");
                 * Double windDegree = observation.getDouble("wind_degrees");
                 * Double windSpeed = observation.getDouble("wind_kph");
                 * String humidity = observation.getString("relative_humidity");
                 * String icon = observation.getString("icon");
                 * String condition = observation.getString("weather");
                 * JSONObject location = observation.getJSONObject("display_location");
                 * String city = location.getString("city");
                 * weather = new Weather(true, degree, windDegree, windSpeed, humidity, icon, condition, city);
                 */
                weather = new Weather(true, Double.parseDouble(jsonString.get(2)), Double.parseDouble(jsonString.get(4)), Double.parseDouble(jsonString.get(3)), jsonString.get(5), jsonString.get(0), jsonString.get(1), jsonString.get(6));
            }
        } catch (Exception e) {
            weather = new Weather(false);
            new StdError("couldn't parse json \n");
        }
        return weather;
    }
}
