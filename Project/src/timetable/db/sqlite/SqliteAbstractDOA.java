package timetable.db.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

class SqliteAbstractDOA {
    private Connection connection;

    SqliteAbstractDOA() {

    }

    SqliteAbstractDOA(Connection connection) {
        this.connection = connection;
    }

    Statement create() throws SQLException {
        return connection.createStatement();
    }

    PreparedStatement prepare(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
