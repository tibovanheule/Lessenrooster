//Tibo Vanheule
package timetable.config;

import javafx.scene.control.Alert;
import timetable.StdError;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

/**
 * Class to read the properties file, if there exist an properties file outside the jar then the class
 * will use that file, else it uses the standard file inside the jar.
 *
 * @author Tibo Vanheule
 */
public class Config {
    /**
     * function to read the properties.
     */
    public Properties getproperties() {
        Reader reader = null;
        Properties properties = new Properties();

        try {
            reader = new FileReader("schedule.out.properties");
            properties.load(reader);
        } catch (IOException e) {
            /*No warning needed*/
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            } else {
                try {
                    /* open standard properties*/
                    properties.load(Config.class.getResourceAsStream("schedule.properties"));
                } catch (IOException e) {
                    new StdError("Error", "Config", "Couldn't read the config file", Alert.AlertType.ERROR);
                }
            }
        }
        return properties;
    }

    /**
     * class to save the properties
     */
    public void saveProperties(Properties properties) {
        try {
            //probeer die op te slaan.
            properties.store(new FileOutputStream("schedule.out.properties"), "Tibo Vanheule\n#Lessenrooster");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
