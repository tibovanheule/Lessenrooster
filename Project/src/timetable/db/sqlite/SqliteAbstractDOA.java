package timetable.db.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class implements most used statements.
 *
 * @author Tibo Vanheule
 */
class SqliteAbstractDOA {
    private Connection connection;

    SqliteAbstractDOA(Connection connection) {
        this.connection = connection;
    }

    /**
     * Create SQL statement
     */
    Statement create() throws SQLException {
        return connection.createStatement();
    }

    /**
     * prepare SQL statement
     */
    PreparedStatement prepare(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
