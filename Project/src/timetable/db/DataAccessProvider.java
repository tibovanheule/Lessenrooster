package timetable.db;

import java.sql.Connection;

public interface DataAccessProvider {
    public DataAccessContext getDataAccessContext() throws DataAccessException;
}
