//Tibo Vanheule
package timetable.db;

import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.objects.Lecture;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class Db {
    private DbConnect system;

    public Db(DbConnect system){
        this.system = system;
    }

    public HashMap<String,Item> getList(String sort){
        HashMap<String, Item> items = new HashMap<>();
        // try met timetable.resources (automatische close)
        //men kan geen column names doorgeven aan een preparedStatement
        //dus wordt het geconcatenate aan de query
        String selection = "SELECT name FROM " + sort;
        try(DataAccessContext dac = new MysqlDataAccessProvider().getDataAccessContext()){
            ItemsDAO itemsDAO = dac.getItemDoa();
            for(Item item: itemsDAO.getList(sort)){
                items.put(item.getName(), item);
            }
        }catch (DataAccessException e){
            System.out.println(e);
        }
        return items;
    }

    public HashMap<String, Item> getFilteredList(String filter){
        HashMap<String,Item> items = new HashMap<>();
        String[] tables = {"teacher","students","location"};
        for (String table:tables) {
            String selection = "SELECT * FROM "+ table +" WHERE name LIKE ?";
            //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
            try (Connection conn = system.connect()) {
                PreparedStatement statement = conn.prepareStatement(selection);
                statement.setString(1, "%" + filter + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    items.put(resultSet.getString("name"),new Item(table, resultSet.getString("name")));
                }
                resultSet.close();
                statement.close();
            } catch (Exception e) {
                System.out.print(e);
            }
        }
        return items;
    }

    public HashMap<Integer,ArrayList<Lecture>> getRooster(String sort, String filter){
        HashMap<Integer,ArrayList<Lecture>> days = new HashMap<>();
        for(int i = 1; i <6;i++) {
            ArrayList<Lecture> lectures = new ArrayList<>();
            // TODO: 29/03/2018 prepared statement
            String selection = "SELECT course, day, students.name AS student, teacher.name AS teacher, location.name AS location, duration, first_block  FROM lecture JOIN students ON lecture.students_id=students.id JOIN teacher on teacher.id=teacher_id " +
                    "JOIN location ON location_id=location.id JOIN period ON first_block=period.id WHERE " + sort + ".name = ? AND day = ?";
            //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
            try (Connection conn = system.connect()) {
                PreparedStatement statement = conn.prepareStatement(selection);
                statement.setString(1, filter);
                statement.setInt(2, i);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Lecture lecture = new Lecture(resultSet.getString("student"), resultSet.getString("teacher"), resultSet.getString("location"),
                            resultSet.getString("course"), resultSet.getInt("day"), resultSet.getInt("first_block"), resultSet.getInt("duration"));
                    lectures.add(lecture);
                }
                resultSet.close();
                days.put(i, lectures);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return days;
    }

}
