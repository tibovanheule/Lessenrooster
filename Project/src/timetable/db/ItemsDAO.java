package timetable.db;

import timetable.objects.Item;

public interface ItemsDAO {
    Iterable<Item> findItem(String sort) throws DataAccessException;
}
