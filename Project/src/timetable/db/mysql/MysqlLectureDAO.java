package timetable.db.mysql;

import timetable.db.DataAccessException;
import timetable.db.LectureDAO;
import timetable.objects.Item;
import timetable.objects.Lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MysqlLectureDAO extends MysqlAbstractDOA implements LectureDAO {

    public MysqlLectureDAO(Connection connection){
        super(connection);
    }

    public Map<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException {
            HashMap<Integer,ArrayList<Lecture>> days = new HashMap<>();
            for(int i = 1; i <6;i++) {
                ArrayList<Lecture> lectures = new ArrayList<>();
                // TODO: 29/03/2018 prepared statement
                String selection = "SELECT course, day, students.name AS student, teacher.name AS teacher, location.name AS location, duration, first_block  FROM lecture JOIN students ON lecture.students_id=students.id JOIN teacher on teacher.id=teacher_id " +
                        "JOIN location ON location_id=location.id JOIN period ON first_block=period.id WHERE " + item.getSort() + ".name = ? AND day = ?";
                //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
                try (PreparedStatement statement = prepare(selection)) {
                    statement.setString(1, item.getName());
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
