/*
package timetable.db.mysql;

import timetable.db.*;

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

    @Override
    public LectureDAO getLectureDoa() {
        return new MysqlLectureDAO(connection);
    }

    @Override
    public PeriodDAO getPeriodDAO() {
        return new MysqlPeriodDAO(connection);
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
*/
