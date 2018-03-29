package timetable.db;


import org.sqlite.SQLiteDataSource;
import timetable.Main;
import timetable.config.Config;

import java.sql.*;
import java.util.Properties;

public class Sqlite implements DbConnect {

    @Override
    public Connection connect(){
        Connection conn = null;
        Config config = new Config();
        Properties properties =  config.getproperties();
        try{
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
