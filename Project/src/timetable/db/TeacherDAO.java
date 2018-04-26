package timetable.db;

import timetable.objects.Item;

public interface TeacherDAO extends DAO {
    Iterable<Item> getTeacher() throws DataAccessException;

    Iterable<Item> getFilteredTeacher(String SearchWord) throws DataAccessException;
}
