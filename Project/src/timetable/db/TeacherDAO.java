package timetable.db;

import timetable.objects.Item;

public interface TeacherDAO extends DAO {
    Iterable<Item> get() throws DataAccessException;

    Iterable<Item> getFiltered(String SearchWord) throws DataAccessException;

}
