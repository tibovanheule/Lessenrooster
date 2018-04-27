package timetable.db;

import timetable.objects.Item;

public interface DAO {
    int delete(Item item) throws DataAccessException;
    Iterable<Item> get() throws DataAccessException;
}
