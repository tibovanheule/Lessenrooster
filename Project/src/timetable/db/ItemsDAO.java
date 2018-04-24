package timetable.db;

import timetable.objects.Item;

public interface ItemsDAO {
    Iterable<Item> getFilterdList(String searchWord) throws DataAccessException;
}
