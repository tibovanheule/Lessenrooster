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
    private String url;

    public SqliteDataAccessProvider(String url) {
        this.url = url;
    }

    public SqliteDataAccessProvider() {
    }

    public DataAccessContext getDataAccessContext() throws DataAccessException {
        try {
            return new SqliteDataAccessContext(getConnection());
        } catch (SQLException e) {
            throw new DataAccessException("Could not create DAC", e);
        }
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        Config config = new Config();
        Properties properties = config.getproperties();

        SQLiteDataSource datasource = new SQLiteDataSource();
        if (url == null) {
            datasource.setUrl(properties.getProperty("DB.sqlite.url"));
        } else {
            datasource.setUrl(url);
        }
        conn = datasource.getConnection();

        return conn;
    }
}
