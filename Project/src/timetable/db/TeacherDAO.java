package timetable.db;

import timetable.objects.Item;

public interface TeacherDAO extends DAO {
    Iterable<Item> get() throws DataAccessException;

    Iterable<Item> getFilteredTeacher(String SearchWord) throws DataAccessException;

    Item createTeacher(String item) throws DataAccessException;

    @Override
    int delete(Item item) throws DataAccessException;
}
