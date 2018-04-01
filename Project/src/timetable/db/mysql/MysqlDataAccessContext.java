package timetable.db.mysql;

import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;
import timetable.db.LectureDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDataAccessContext implements DataAccessContext {

    private Connection connection;

    MysqlDataAccessContext(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ItemsDAO getItemDoa() {
        return new MysqlItemsDAO(connection);
    }

    public LectureDAO getLectureDoa() {
        return new MysqlLectureDAO(connection);
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
