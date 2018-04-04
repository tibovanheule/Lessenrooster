//Tibo Vanheule
package timetable.weather;

import timetable.StdError;
import timetable.config.Config;
import timetable.objects.Weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class WeatherScraper {
    private Weather weather = null;
    /*
     * Normaal Zou ik een JSON Library gebruikt hebben en rechtsteeks met de api gewerkt hebben. Deze is ook al geïmplementeerd (zie beneden, jar die gebruikt ik zou hebben ook toegevoegd in zip),
     * Maar om een extra library (Voor de JSON) te vermijden en om mij mogelijke Indianio problemen ,ten gevolge van een Extra library, te besparen.
     * Laat ik mijn web server het werk nu doen. ik geef aan de url de authenticatie sleutel van api mee en ook de Stadsnaam (via URL parameters)
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

    private String[] getData() {
        //stringbuilder zodat we later de inputline er aan toe kunnen voegen (sneller dan string concatenatie += )
        String[] stringsArray = new String[7];
        try {
            Config config = new Config();
            Properties properties = config.getproperties();
            String key = properties.getProperty("weather.key");
            String city = properties.getProperty("weather.city");
            URL weather = new URL("https://depaddestoeltjes.be/share/wundergroundApi.php?key=" + key + "&city=" + city);
            String inputLine;
            BufferedReader input = new BufferedReader(new InputStreamReader(weather.openStream()));
            int count = 0;
            while ((inputLine = input.readLine()) != null) {
                stringsArray[count] = inputLine;
                count++;
            }
        } catch (Exception e) {
            weather = new Weather(false);
            /*e.printStackTrace();*/
            new StdError("WARNING! couldn't get the weather! Do you have an internet connection?");

        }
        return stringsArray;
    }

    public Weather getWeather() {
        try {
            String[] jsonString = getData();
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
                weather = new Weather(true, Double.parseDouble(jsonString[2]), Double.parseDouble(jsonString[4]), Double.parseDouble(jsonString[3]), jsonString[5], jsonString[0], jsonString[1], jsonString[6]);
            }
        } catch (Exception e) {
            weather = new Weather(false);
            /*e.printStackTrace();*/
        }
        return weather;
    }
}
