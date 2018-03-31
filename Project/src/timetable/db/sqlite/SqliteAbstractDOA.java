package timetable.db.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteAbstractDOA {
    private Connection connection;

    public SqliteAbstractDOA(){

    }

    public SqliteAbstractDOA(Connection connection){
        this.connection = connection;
    }

    protected Statement create() throws SQLException{
        return connection.createStatement();
    }

    protected PreparedStatement prepare(String sql) throws SQLException{
        return connection.prepareStatement(sql);
    }
}