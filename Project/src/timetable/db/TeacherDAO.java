package timetable.db;

import timetable.objects.Item;

/**
 * specifies methods of TeacherDAO
 *
 * @author Tibo Vanheule
 */
public interface TeacherDAO extends DAO {
    Iterable<Item> get() throws DataAccessException;

    Iterable<Item> getFiltered(String SearchWord) throws DataAccessException;

}
