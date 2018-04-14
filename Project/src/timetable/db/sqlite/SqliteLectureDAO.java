package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.LectureDAO;
import timetable.objects.Item;
import timetable.objects.Lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class SqliteLectureDAO extends SqliteAbstractDOA implements LectureDAO {
    SqliteLectureDAO(Connection connection) {
        super(connection);
    }

    public HashMap<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException {
        HashMap<Integer, ArrayList<Lecture>> days = new HashMap<>();
        for (int i = 1; i < 6; i++) {
            ArrayList<Lecture> lectures = new ArrayList<>();
            // TODO: 29/03/2018 prepared statement
            String selection;
            if (item.getSort().equals("lecture")) {
                selection = "SELECT course, day, students.name AS student, teacher.name AS teacher, location.name AS location, duration, first_block, hour , minute FROM lecture JOIN students ON lecture.students_id=students.id JOIN teacher on teacher.id=teacher_id " +
                        "JOIN location ON location_id=location.id JOIN period ON first_block=period.id WHERE course = ? AND day = ? ORDER BY first_block";

            } else {
                selection = "SELECT course, day, students.name AS student, teacher.name AS teacher, location.name AS location, duration, first_block, hour , minute FROM lecture JOIN students ON lecture.students_id=students.id JOIN teacher on teacher.id=teacher_id " +
                        "JOIN location ON location_id=location.id JOIN period ON first_block=period.id WHERE " + item.getSort() + ".name = ? AND day = ? ORDER BY first_block";
                //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
            }
            try (PreparedStatement statement = prepare(selection)) {
                statement.setString(1, item.getName());
                statement.setInt(2, i);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Lecture lecture = new Lecture(resultSet.getString("student"), resultSet.getString("teacher"), resultSet.getString("location"),
                            resultSet.getString("course"), resultSet.getInt("day"), resultSet.getInt("first_block"), resultSet.getInt("duration"), resultSet.getString("hour") + ":" + resultSet.getString("minute"));

                    lectures.add(lecture);
                }
                resultSet.close();
                // Sortering (lessen in de juiste volgorde zetten) Dit is wel niet meer nodig omdat dit werk nu uitbesteed wordt aan de DB (zie sql query)
                //lectures.sort(Comparator.comparing(Lecture::getBlock));
                /*for (Lecture lecture : lectures) {
                    String conflict = "SELECT course, day, students.name AS student, teacher.name AS teacher, location.name AS location, duration, first_block FROM lecture JOIN students ON lecture.students_id=students.id JOIN teacher on teacher.id=teacher_id " +
                            "JOIN location ON location_id=location.id WHERE day = ? AND ( ? >=first_block  )) AND " + item.getSort() + ".name = ? AND NOT course = ?";

                    try (PreparedStatement conflicts = prepare(conflict)) {
                        conflicts.setString(5, lecture.getCourse());
                        conflicts.setInt(1, lecture.getDay());
                        conflicts.setInt(2, lecture.getBlock());
                        *//*ik doe het einduur -1 omdat between inclusive is*//*
                        conflicts.setInt(3, lecture.getBlock() + lecture.getDuration()  );
                        conflicts.setString(4, item.getName());
                        ResultSet resultSet2 = conflicts.executeQuery();
                        if(resultSet2.next()){
                            lecture.setConflict(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/

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
                /*e.printStackTrace();*/
                throw new DataAccessException("could't get lectures", e);
            }
        }
        return days;
    }
}
