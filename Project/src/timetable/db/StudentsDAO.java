package timetable.db;

import timetable.objects.Item;

public interface StudentsDAO extends DAO {
    Iterable<Item> get() throws DataAccessException;

    Iterable<Item> getFilteredStudent(String SearchWord) throws DataAccessException;

    int delete(Item item) throws DataAccessException;
}
