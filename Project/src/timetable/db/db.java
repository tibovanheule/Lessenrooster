package timetable.db;

import timetable.Main;

import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class db {
    private static Connection connect(){
        Connection conn = null;
        Properties properties = new Properties();
        try{
            //properties in laden voor DB url op te halen
            properties.load(Main.class.getResourceAsStream("lessenrooster.properties"));
            //open een verbinding
            conn = DriverManager.getConnection(properties.getProperty("DB.sqlite.url"));
        }catch (Exception e){
            System.out.print(e);
        }
        return conn;
    }

    public static ArrayList<String> getList(String sort){
        ArrayList<String> students = new ArrayList<>();
        //roep methode connect() op om een verbinding te maken
        Connection conn = connect();
        // try met resources (automatische close)
        String sql = "SELECT name FROM ?";
        try(conn){
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, sort);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                students.add(resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
        }catch (Exception e){
            //fout weergeven in de lijst.
            students.add("We can't query the database, please check if the database is running!");
            System.out.print(e);
        }
        return students;
    }
}
