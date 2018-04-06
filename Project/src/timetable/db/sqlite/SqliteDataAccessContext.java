package timetable.db.sqlite;

import timetable.db.*;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteDataAccessContext implements DataAccessContext {

    private Connection connection;

    SqliteDataAccessContext(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ItemsDAO getItemDoa() {
        return new SqliteItemsDAO(connection);
    }

    @Override
    public LectureDAO getLectureDoa() {
        return new SqliteLectureDAO(connection);
    }

    @Override
    public PeriodDAO getPeriodDAO() {
        return new SqlitePeriodDAO(connection);
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            new DataAccessException("couldn't close the connection :o", e).printStackTrace();
        }
    }
}
