package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.LectureDAO;
import timetable.objects.Item;
import timetable.objects.Lecture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
            // TODO: 29/03/2018 queries
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
                            resultSet.getString("course"), resultSet.getInt("day"), resultSet.getInt("first_block"), resultSet.getInt("duration"), resultSet.getInt("hour"), resultSet.getInt("minute"));

                    lectures.add(lecture);
                }
                resultSet.close();


                int j = 0;
                while (j < lectures.size()) {
                    int k = 0;
                    Lecture lecture1 = lectures.get(j);
                    while (k < lectures.size()) {
                        if (k != j) {
                            Lecture lecture2 = lectures.get(k);
                            if (lecture1.compare(lecture1, lecture2) <= 0) {
                                lecture1.addConflict(lecture2);
                                lecture1.setConflict(true);
                                lecture2.setConflict(true);
                            }
                        }
                        k++;
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

    @Override
    public Iterable<Item> getLectures() throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String selection = "select distinct course as name from lecture";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (Statement statement = create()) {
            ResultSet resultSet = statement.executeQuery(selection);
            while (resultSet.next()) {
                /*sinds dat vakken niet gedeleted worden a.d.h.v. het id en die ook geen hebben geven we null eraan mee*/
                items.add(new Item("lecture", resultSet.getString("name"), null));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }

    @Override
    public Iterable<Item> getFilteredLectures(String searchText) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String sql = "SELECT DISTINCT course FROM lecture where course like ?";
        try (PreparedStatement statement = prepare(sql)) {
            statement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("lecture", resultSet.getString("course"), null));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }
}
