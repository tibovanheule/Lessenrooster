package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class SqliteItemsDAO extends SqliteAbstractDOA implements ItemsDAO {
    SqliteItemsDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Iterable<Item> getFilterdList(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String[] tables = {"teacher", "students", "location"};
        for (String table : tables) {
            String selection = "SELECT * FROM " + table + " WHERE name LIKE ?";
            //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
            try (PreparedStatement statement = prepare(selection)) {
                statement.setString(1, "%" + searchWord + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    items.add(new Item(table, resultSet.getString("name"), resultSet.getInt("id")));
                }
                resultSet.close();
            } catch (Exception e) {
                throw new DataAccessException("could not retrieve items", e);
            }
        }
        /* Extra query voor de lessen*/
        String sql = "SELECT DISTINCT course FROM lecture where course like ?";
        try (PreparedStatement statement = prepare(sql)) {
            statement.setString(1, "%" + searchWord + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("lecture", resultSet.getString("course"), null));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }
}
