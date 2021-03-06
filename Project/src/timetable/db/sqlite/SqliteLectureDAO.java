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
            String selection = "SELECT teacher_id, location_id, lecture.students_id, course, day, students.name AS student, teacher.name AS teacher, " +
                    "location.name AS location, duration, first_block, hour , minute FROM lecture JOIN students ON lecture.students_id=students.id" +
                    " JOIN teacher on teacher.id=teacher_id " +
                    "JOIN location ON location_id=location.id JOIN period ON first_block=period.id WHERE ";
            if (item.getSort().equals("lecture")) {
                selection += "course = ? AND day = ? ORDER BY first_block";
            } else {
                selection += item.getSort() + ".name = ? AND day = ? ORDER BY first_block";
            }
            try (PreparedStatement statement = prepare(selection)) {
                statement.setString(1, item.getName());
                statement.setInt(2, i);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Lecture lecture = new Lecture(resultSet.getString("student"),
                            resultSet.getString("teacher"),
                            resultSet.getString("location"),
                            resultSet.getString("course"),
                            resultSet.getInt("day"),
                            resultSet.getInt("first_block"),
                            resultSet.getInt("duration"),
                            resultSet.getInt("hour"),
                            resultSet.getInt("minute"),
                            resultSet.getInt("students_id"),
                            resultSet.getInt("teacher_id"),
                            resultSet.getInt("location_id")
                    );
                    lectures.add(lecture);
                }
                resultSet.close();

                /*conflict check*/
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
                e.printStackTrace();
                throw new DataAccessException("couldn't get lecture'", e);
            }
        }
        return days;
    }

    @Override
    public Iterable<Item> get() throws DataAccessException {
        ArrayList<Item> items = new ArrayList<>();
        String selection = "select distinct course as name from lecture";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (Statement statement = create()) {
            ResultSet resultSet = statement.executeQuery(selection);
            while (resultSet.next()) {
                items.add(new Item("lecture", resultSet.getString("name")));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }

    @Override
    public Iterable<Item> getFiltered(String searchText) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<>();
        String sql = "SELECT DISTINCT course FROM lecture where course like ?";
        try (PreparedStatement statement = prepare(sql)) {
            statement.setString(1, "%" + searchText + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("lecture", resultSet.getString("course")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve filtered lectures items", e);
        }
        return items;
    }

    @Override
    public int delete(Item item) throws DataAccessException {
        /*to comply with DAO interface, never used*/
        return 0;
    }

    @Override
    public int delete(Lecture item) throws DataAccessException {
        String delete = "DELETE FROM lecture WHERE course = ? AND day = ? AND students_id = ? AND teacher_id = ? AND" +
                " first_block = ? AND location_id = ? AND duration = ?";

        try (PreparedStatement statement = prepare(delete)) {
            statement.setString(1, item.getCourse());
            statement.setInt(2, item.getDay());
            statement.setInt(3, item.getStudentId());
            statement.setInt(4, item.getTeacherId());
            statement.setInt(5, item.getBlock());
            statement.setInt(6, item.getLocationId());
            statement.setInt(7, item.getDuration());
            statement.execute();
        } catch (Exception e) {
            throw new DataAccessException("could not delete lecture", e);
        }
        return 0;
    }


    @Override
    public Item create(String item) {
        /*to comply with DAO interface, not used*/
        return null;
    }

    @Override
    public int create(Lecture lecture) throws DataAccessException {
        String insert = "INSERT INTO lecture (course,first_block,students_id,location_id,teacher_id,day,duration) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(1, lecture.getCourse());
            statement.setInt(2, lecture.getBlock());
            statement.setInt(3, lecture.getStudentId());
            statement.setInt(4, lecture.getLocationId());
            statement.setInt(5, lecture.getTeacherId());
            statement.setInt(6, lecture.getDay());
            statement.setInt(7, lecture.getDuration());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("could not create lecture", e);
        }
        return 0;
    }

    @Override
    public int update(Lecture newLecture, Lecture oldLecture) throws DataAccessException {
        String insert = "UPDATE lecture SET course=?,first_block=?,students_id=?,location_id=?,teacher_id=?,day=?,duration=? " +
                "WHERE course=? AND first_block=? AND students_id=? AND location_id=? AND teacher_id=? AND day=? AND duration=?";
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(1, newLecture.getCourse());
            statement.setInt(2, newLecture.getBlock());
            statement.setInt(3, newLecture.getStudentId());
            statement.setInt(4, newLecture.getLocationId());
            statement.setInt(5, newLecture.getTeacherId());
            statement.setInt(6, newLecture.getDay());
            statement.setInt(7, newLecture.getDuration());

            statement.setString(8, oldLecture.getCourse());
            statement.setInt(9, oldLecture.getBlock());
            statement.setInt(10, oldLecture.getStudentId());
            statement.setInt(11, oldLecture.getLocationId());
            statement.setInt(12, oldLecture.getTeacherId());
            statement.setInt(13, oldLecture.getDay());
            statement.setInt(14, oldLecture.getDuration());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("could not update lecture", e);
        }
        return 0;
    }

    @Override
    public int updateName(Item item) {
        /*to comply with DAO*/
        return 0;
    }


    /**
     * to check if a name isn't already in database
     */
    @Override
    public Boolean nameExists(String name) {
        return false;
    }

    // TODO: 6/05/2018 expand to all

    /**
     * to check if there is a lesson at the same time
     */
    @Override
    public Boolean conflict(Lecture lecture) throws DataAccessException {
        String search = "SELECT course FROM lecture WHERE first_block=? AND day=? LIMIT 1";
        try (PreparedStatement statement = prepare(search)) {
            statement.setInt(1, lecture.getBlock());
            statement.setInt(2, lecture.getDay());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            throw new DataAccessException("couldn't check conflict", e);
        }
        return false;
    }
}
