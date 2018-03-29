package timetable.db.mysql;

import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDataAccessContext implements DataAccessContext {

    private Connection connection;

    public MysqlDataAccessContext(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ItemsDAO getItemDoa() {
        return new MysqlItemsDAO(connection);
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
