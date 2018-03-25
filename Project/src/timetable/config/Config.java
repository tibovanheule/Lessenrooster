//Tibo Vanheule
package timetable.config;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Config{
    public Properties getproperties(){
        //initialisatie
        Reader reader = null;
        Properties properties = new Properties();

        //probeer het bestand in te lezen
        try{
            reader = new FileReader( "schedule.out.properties" );
            //inladen
            properties.load(reader);
        }catch(IOException e) {
            //System.out.println(e);
        }finally{
            if (reader != null){
                //als de reader niet null is, sluit die
                try{
                    reader.close();
                } catch (IOException e){
                    //System.out.println(e);
                }
                //anders als reader null is dan bestaat het bestand niet of is niet leesbaar
            }else{
                try{
                    //open standaard properties
                    properties.load(Config.class.getResourceAsStream("schedule.properties"));
                }catch(IOException e) {
                    //System.out.println(e);
                }
            }
        }
        return properties;
    }
    public void saveProperties(Properties properties){
        try{
            //probeer die op te slaan.
            properties.store(new FileOutputStream("schedule.out.properties"), "Tibo Vanheule\n#Lessenrooster");
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
