//Tibo Vanheule
package timetable.weather;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import timetable.Main;
import timetable.objects.Weather;

/**
 * Companion to display the weather data in a stage
 *
 * @author Tibo Vanheule
 */
public class WeatherController {
    @FXML
    private Label temp, humid, condition, windSpeed;
    @FXML
    private ImageView weatherIcon, arrow;
    private Stage stage;


    /**
     * function to set the stage, it it can be used in close function
     */
    public void setStageAndSetupListeners(Stage stage) {
        //krijgen van de stage
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

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(weather.getConnection());
        }

    }

    /**
     * close the stage
     */
    public void close() {
        //afsluiten stage
        stage.close();
    }
}
