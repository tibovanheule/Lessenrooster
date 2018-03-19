package timetable.db;


import org.sqlite.SQLiteDataSource;
import timetable.Main;
import java.sql.*;
import java.util.Properties;

public class Sqlite implements DbConnect {

    @Override
    public Connection connect(){
        Connection conn = null;
        Properties properties = new Properties();
        try{
            //properties in laden voor DB url op te halen
            properties.load(Main.class.getResourceAsStream("schedule.properties"));
            //open een verbinding
            SQLiteDataSource datasource = new SQLiteDataSource();
            datasource.setUrl(properties.getProperty("DB.sqlite.url"));
            conn = datasource.getConnection();
        }catch (Exception e){
          System.out.println(e);
        }
        return conn;
    }
}
