package timetable.db.sqlite;

import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;
import timetable.db.LectureDAO;
import timetable.db.mysql.MysqlLectureDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class SqliteDataAccessContext implements DataAccessContext {

    private Connection connection;

    public SqliteDataAccessContext(Connection connection) {
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
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            new DataAccessException("couldn't close the connection :o", e).printStackTrace();
        }
    }
}
