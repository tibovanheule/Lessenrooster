//tibo Vanheule
package timetable.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Db {
    private DbConnect system;

    public Db(DbConnect system){
        this.system = system;
    }

    public ArrayList<String> getList(String sort){
        ArrayList<String> students = new ArrayList<>();
        // try met resources (automatische close)
        //men kan geen column names doorgeven aan een preparedStatement
        //dus wordt het geconcatenate aan de query
        String selection = "SELECT name FROM " + sort;
        try(Connection conn = system.connect()){
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

    public ArrayList<String> getFilteredList(String filter){
        ArrayList<String> students = new ArrayList<>();
        String selection = "SELECT * FROM teacher WHERE name LIKE ?"
                + " UNION SELECT * FROM students WHERE name LIKE ?"
                + " UNION SELECT * FROM location WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try(Connection conn = system.connect()){
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
