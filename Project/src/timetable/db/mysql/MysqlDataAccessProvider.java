/*
package timetable.db.mysql;

import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;

import java.sql.Connection;
import java.sql.SQLException;

public class MysqlDataAccessProvider implements DataAccessProvider {
    */
/*
   Ik had al een mysql-server draaiende, ik vond het dan het dan ook eens leuk om een mysql-implementatie te maken.
   Deze is opgevuld met dezelfde data als de sqlite db(aan de hand van een aangepaste lectures.sql bestand).
   De sqlite en de mysql staan los van elkaar en worden niet gesynchroniseerd met elkaar.
   De mysql verbinding gebeurd onder een account met maar één privelege (SELECT) op één db (Main).
   Er zijn ook maar 3 simultane verbindingen mogelijk. ( mss een belangrijk gegeven bij het testen :) )
    *//*

    public DataAccessContext getDataAccessContext() throws DataAccessException {
        try {
            return new MysqlDataAccessContext(getConnection());
        } catch (SQLException e) {
            throw new DataAccessException("Could not create DAC", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
*/
/*        Config config = new Config();
        Properties properties = config.getproperties();
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser(properties.getProperty("DB.mysql.user"));
        dataSource.setPassword(properties.getProperty("DB.mysql.password"));
        dataSource.setURL(properties.getProperty("DB.mysql.url"));
        conn = dataSource.getConnection();*//*

        return conn;
    }
}
*/
