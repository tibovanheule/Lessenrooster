package timetable.db;

import timetable.objects.Item;

public interface LocationDAO extends DAO {
    Iterable<Item> getLocation() throws DataAccessException;

    Iterable<Item> getFilteredLocation(String SearchWord) throws DataAccessException;
}
