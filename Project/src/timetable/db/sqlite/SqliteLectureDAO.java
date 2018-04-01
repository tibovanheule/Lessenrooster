package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.LectureDAO;
import timetable.objects.Item;
import timetable.objects.Lecture;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SqliteLectureDAO extends SqliteAbstractDOA implements LectureDAO {
    public Map<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException {
        HashMap<Integer, ArrayList<Lecture>> days = new HashMap<>();
        for (int i = 1; i < 6; i++) {
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
                // Sortering (lessen in de juiste volgorde zetten) Dit is wel niet meer nodig omdat dit werk nu uitbesteed wordt aan de DB (zie sql query)
                //lectures.sort(Comparator.comparing(Lecture::getBlock));
                int j = 0;
                while (j < lectures.size() - 1) {
                    Lecture lecture1 = lectures.get(j);
                    Lecture lecture2 = lectures.get(j + 1);
                    if (lecture1.getBlock().equals(lecture2.getBlock()) || lecture2.getBlock() < lecture1.getBlock() + lecture1.getDuration()) {
                        lecture1.setConflict(true);
                        lecture2.setConflict(true);
                    }
                    j++;
                }
                days.put(i, lectures);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return days;
    }
}
