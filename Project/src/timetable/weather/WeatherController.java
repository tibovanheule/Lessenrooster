//Tibo Vanheule
package timetable.weather;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import timetable.Main;
import timetable.objects.Weather;

public class WeatherController {
    public Label text;
    public ImageView weatherIcon;
    private Stage stage;

    public void setStageAndSetupListeners(Stage stage){
        //krijgen van de stage
        this.stage = stage;
    }

    public void initialize(){
        Weather weather = new WeatherScraper().getWeather();

        Image icon = new Image(Main.class.getResourceAsStream("resources/images/weather/"+weather.getIcon()+".png"));
        weatherIcon.setImage(icon);

        text.setText(weather.getTemp() + weather.getWindSpeed() + weather.getWindDegree() + weather.getHumidity());
    }

    public void close(){
        //afsluiten stage
        stage.close();
    }
}
