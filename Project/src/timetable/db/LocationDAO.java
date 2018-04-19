package timetable.db;

import timetable.objects.Item;

public interface LocationDAO {
    Iterable<Item> getLocation() throws DataAccessException;

    Iterable<Item> getFilteredLocation(String SearchWord) throws DataAccessException;
}
