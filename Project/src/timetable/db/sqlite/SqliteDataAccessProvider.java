package timetable.db.sqlite;

import org.sqlite.SQLiteDataSource;
import timetable.config.Config;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * class sets up connection to database and gives back the DataAccessContext
 *
 * @author Tibo Vanheule
 */
public class SqliteDataAccessProvider implements DataAccessProvider {
    private String url;

    /**
     * url parameter -> will use parameter to create sqlite Database
     */
    public SqliteDataAccessProvider(String url) {
        this.url = url;
    }

    /**
     * no parameter -> will use standard sqlite database
     */
    public SqliteDataAccessProvider() {
    }

    /**
     * creates DataAccessContext
     */
    public DataAccessContext getDataAccessContext() throws DataAccessException {
        try {
            return new SqliteDataAccessContext(getConnection());
        } catch (SQLException e) {
            throw new DataAccessException("Could not create DAC", e);
        }
    }

    /**
     * Makes a connection to the database
     */
    private Connection getConnection() throws SQLException {
        Config config = new Config();
        Properties properties = config.getproperties();

        SQLiteDataSource datasource = new SQLiteDataSource();
        try{
            /*test will give null pointer if null*/
            datasource.setUrl(url.toString());
        }catch (NullPointerException e){
            datasource.setUrl(properties.getProperty("DB.sqlite.url"));
        }

        return datasource.getConnection();

    }
}
