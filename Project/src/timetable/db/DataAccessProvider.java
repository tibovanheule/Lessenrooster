package timetable.db;

public interface DataAccessProvider {
    DataAccessContext getDataAccessContext() throws DataAccessException;
}
