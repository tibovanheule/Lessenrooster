package timetable.db.sqlite;

import org.sqlite.SQLiteDataSource;
import timetable.config.Config;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class SqliteDataAccessProvider implements DataAccessProvider {
    public DataAccessContext getDataAccessContext() throws DataAccessException {
        try {
            return new SqliteDataAccessContext(getConnection());
        } catch (SQLException e) {
            throw new DataAccessException("Could not create DAC", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn;
        Config config = new Config();
        Properties properties = config.getproperties();

        SQLiteDataSource datasource = new SQLiteDataSource();
        datasource.setUrl(properties.getProperty("DB.sqlite.url"));
        conn = datasource.getConnection();

        return conn;
    }
}
