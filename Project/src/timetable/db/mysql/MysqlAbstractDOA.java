package timetable.db.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlAbstractDOA {
    private Connection connection;

    public MysqlAbstractDOA(){

    }

    public MysqlAbstractDOA(Connection connection){
        this.connection = connection;
    }

    protected Statement create() throws SQLException{
        return connection.createStatement();
    }

    protected PreparedStatement prepare(String sql) throws SQLException{
        return connection.prepareStatement(sql);
    }
}
