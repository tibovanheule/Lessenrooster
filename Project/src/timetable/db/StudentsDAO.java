package timetable.db;

import timetable.objects.Item;

/**
 * specifies methods of StudentsDAO
 *
 * @author Tibo Vanheule
 */
public interface StudentsDAO extends DAO {
    Iterable<Item> get() throws DataAccessException;

    Iterable<Item> getFiltered(String SearchWord) throws DataAccessException;

    int delete(Item item) throws DataAccessException;
}
