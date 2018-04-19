package timetable.db;

import timetable.objects.Item;

public interface TeacherDAO {
    Iterable<Item> getTeacher() throws DataAccessException;

    Iterable<Item> getFilteredTeacher(String SearchWord) throws DataAccessException;
}
