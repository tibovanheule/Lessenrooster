package timetable.db.sqlite;

import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;

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
    public void close() {
        try {
            connection.close();
        }catch (SQLException e){
            System.out.println(new DataAccessException("couldn't close the connection :o", e));
        }
    }
}
