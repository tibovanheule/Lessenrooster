package timetable.db;

import timetable.objects.Item;

/**
 * Interface to specify standard procedures.
 * @author Tibo Vanheule*/
public interface DAO {
    int delete(Item item) throws DataAccessException;

    Iterable<Item> get() throws DataAccessException;

    Item create(String item) throws DataAccessException;

    int updateName(Item item) throws DataAccessException;

    Iterable<Item> getFiltered(String searchWord) throws DataAccessException;
}
