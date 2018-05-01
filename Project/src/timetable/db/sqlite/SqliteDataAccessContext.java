package timetable.db.sqlite;

import timetable.db.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Class implements DataAccessContext, gives back the DAO that you need
 *
 * @author Tibo Vanheule
 */
public class SqliteDataAccessContext implements DataAccessContext {

    private Connection connection;

    /**
     * sets the connection field
     */
    SqliteDataAccessContext(Connection connection) {
        this.connection = connection;
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
    public StudentsDAO getStudentsDAO() {
        return new SqliteStudentsDAO(connection);
    }

    @Override
    public LocationDAO getLocationDAO() {
        return new SqliteLocationDAO(connection);
    }

    @Override
    public TeacherDAO getTeacherDAO() {
        return new SqliteTeacherDAO(connection);
    }

    /**
     * Closes connection
     */
    @Override
    public void close() throws DataAccessException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DataAccessException("couldn't close the connection :o", e);
        }
    }
}
