package timetable.db;

import timetable.objects.Item;

public interface DAO {
    int delete(Item item) throws DataAccessException;

    Iterable<Item> get() throws DataAccessException;

    Item create(String item) throws DataAccessException;

    int updateName(Item item) throws DataAccessException;
}
