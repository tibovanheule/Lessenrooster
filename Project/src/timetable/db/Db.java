//tibo Vanheule
package timetable.db;

import timetable.objects.Lecture;
import timetable.objects.Item;

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

    public ArrayList<Item> getList(String sort){
        ArrayList<Item> students = new ArrayList<>();
        // try met timetable.resources (automatische close)
        //men kan geen column names doorgeven aan een preparedStatement
        //dus wordt het geconcatenate aan de query
        String selection = "SELECT name FROM " + sort;
        try(Connection conn = system.connect()){
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selection);
            while (resultSet.next()){
                students.add(new Item(sort ,resultSet.getString("name")));
            }

            resultSet.close();
            statement.close();
        }catch (Exception e){
            //foutmelding weergeven in de lijst.
            System.out.print(e);
        }
        return students;
    }

    public ArrayList<Item> getFilteredList(String filter){
        ArrayList<Item> items = new ArrayList<>();
        String[] tables = {"teacher","students","location"};
        for (String table:tables) {
            String selection = "SELECT * FROM "+ table +" WHERE name LIKE ?";
            //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
            try (Connection conn = system.connect()) {
                PreparedStatement statement = conn.prepareStatement(selection);
                statement.setString(1, "%" + filter + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    items.add(new Item(table, resultSet.getString("name")));
                }
                resultSet.close();
                statement.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }
        return items;
    }

    public ArrayList<Lecture> getRooster(String sort, String filter){
        ArrayList<Lecture> lectures = new ArrayList<>();
        String selection = "SELECT * FROM lecture JOIN students ON lecture.students_id=students.id JOIN teacher on teacher.id=teacher_id " +
                "JOIN location ON location_id=location.id JOIN period ON first_block=period.id WHERE "+sort+".name = ? ";
        System.out.println(selection);
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try(Connection conn = system.connect()){
            PreparedStatement statement = conn.prepareStatement(selection);
            statement.setString(1, filter );
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                Lecture test = new Lecture(resultSet.getString("name"), resultSet.getString("name"), resultSet.getString("name"),
                        resultSet.getString("course"), resultSet.getInt("day"),resultSet.getInt("first_block"), resultSet.getInt("duration"));

                lectures.add(test);
            }
            resultSet.close();
            statement.close();
        }catch (Exception e){
            System.out.print(e);
        }
        return lectures;
    }

}
