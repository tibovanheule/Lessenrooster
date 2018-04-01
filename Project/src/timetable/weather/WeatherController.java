//Tibo Vanheule
package timetable.weather;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import timetable.Main;
import timetable.objects.Weather;

public class WeatherController {
    public Label temp, humid, condition, windSpeed;
    public ImageView weatherIcon, arrow, compass;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage) {
        //krijgen van de stage
        this.stage = stage;
    }

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

    public void close() {
        //afsluiten stage
        stage.close();
    }
}
