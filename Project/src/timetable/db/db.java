//tibo Vanheule
package timetable.db;


import java.sql.*;

import timetable.Main;

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
        // try met resources (automatische close)
        //men kan geen column names doorgeven aan een preparedStatement
        //dus wordt het geconcatenate aan de query
        String selection = "SELECT name FROM " + sort;
        try(Connection conn = connect()){
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selection);
            while (resultSet.next()){
                students.add(resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
        }catch (Exception e){
            //foutmelding weergeven in de lijst.
            students.add("We can't query the database, please check if the database is running!");
            System.out.print(e);
        }
        return students;
    }

    public static ArrayList<String> getFilteredList(String filter){
        ArrayList<String> students = new ArrayList<>();
        String selection = "SELECT * FROM teacher WHERE name LIKE ?"
                + " UNION SELECT * FROM students WHERE name LIKE ?"
                + " UNION SELECT * FROM location WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try(Connection conn = connect()){
            PreparedStatement statement = conn.prepareStatement(selection);
            statement.setString(1, "%" + filter + "%");
            statement.setString(2, "%" + filter + "%");
            statement.setString(3, "%" + filter + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                students.add(resultSet.getString("name"));
            }
            resultSet.close();
            statement.close();
        }catch (Exception e){
            //foutmelding weergeven in de lijst.
            students.add("We can't query the database, please check if the database is running!");
            System.out.print(e);
        }
        return students;
    }
}
