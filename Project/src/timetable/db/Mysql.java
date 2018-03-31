//Tibo Vanheule
package timetable.db;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import timetable.config.Config;

import java.sql.Connection;
import java.util.Properties;

public class Mysql implements DbConnect{
    /*
    Ik had al een mysql-server draaiende, ik vond het dan het dan ook eens leuk om een mysql-implementatie te maken.
    Deze is opgevuld met dezelfde data als de sqlite db(aan de hand van een aangepaste lectures.sql).
    De sqlite en de mysql staan los van elkaar en worden niet gesynchroniseerd met elkaar.
    De mysql verbinding gebreurd onder een acc met maar één privelege (SELECT) op één db (Main).
    Er zijn ook maar 2 simultane verbindingen mogelijk. ( mss een belangrijk gegeven bij het testen :) )
     */
    @Override
    public Connection connect() {
        Connection conn = null;
        //op halen config bestand
        Config config = new Config();
        Properties properties =  config.getproperties();
        try{
            //open een verbinding
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser(properties.getProperty("DB.mysql.user"));
            dataSource.setPassword(properties.getProperty("DB.mysql.password"));
            dataSource.setURL(properties.getProperty("DB.mysql.url"));
            conn = dataSource.getConnection();
        }catch (Exception e){
            System.out.print(e);
        }
        return conn;
    }
}
