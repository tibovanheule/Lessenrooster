package timetable.db;

import timetable.objects.Item;

public interface ItemsDAO {
    Iterable<Item> getList() throws DataAccessException;

    Iterable<Item> getFilterdList(String searchWord) throws DataAccessException;
}
