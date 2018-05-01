package timetable.db;

/**
 * Interface specifies methods of data access provider
 *
 * @author Tibo Vanheule
 */
public interface DataAccessProvider {
    DataAccessContext getDataAccessContext() throws DataAccessException;
}
