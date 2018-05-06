//Tibo Vanheule
package timetable.weather;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import timetable.Main;
import timetable.StdError;
import timetable.objects.Weather;

/**
 * Companion to display the weather data in a stage
 *
 * @author Tibo Vanheule
 */
public class WeatherController {
    @FXML
    private Label temp, humid, condition, windSpeed, city;
    @FXML
    private ImageView weatherIcon, arrow;
    private Stage stage;


    /**
     * function to set the stage, it it can be used in close function
     */
    public void setStageAndSetupListeners(Stage stage) {
        this.stage = stage;
    }

    /**
     * initialize stage, set all Labels with the weather datat
     */
    public void initialize() {

        Weather weather = new WeatherScraper().getWeather();
        try {
            Image icon = new Image(Main.class.getResourceAsStream("resources/images/weather/" + weather.getIcon() + ".png"));
            weatherIcon.setImage(icon);
            arrow.setRotate(arrow.getRotate() + weather.getWindDegree());
            condition.setText(weather.getCondition());
            temp.setText(weather.getTemp() + "°");
            humid.setText(weather.getHumidity());
            windSpeed.setText("Windspeed: " + weather.getWindSpeed() + "kph");
            city.setText(weather.getCity());

        } catch (Exception e) {
            new StdError("couldn't get weather data");
        }

    }

    /**
     * close the stage
     */
    public void close() {
        stage.close();
    }
}
