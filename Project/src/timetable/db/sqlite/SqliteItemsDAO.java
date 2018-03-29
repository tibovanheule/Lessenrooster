package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteItemsDAO extends SqliteAbstractDOA implements ItemsDAO {
    public SqliteItemsDAO(Connection connection){
        super(connection);
    }

    @Override
    public Iterable<Item> findItem(String sort) throws DataAccessException {
        Iterable<Item> items = new ArrayList<>();
        String selection = "SELECT name FROM " + sort;
        try(Statement statement = create(); ResultSet resultSet = statement.executeQuery(selection)){
            while (resultSet.next()){
                ((ArrayList<Item>) items).add(new Item(sort,resultSet.getString("name")));
            }
        }catch (Exception e){
        //foutmelding weergeven in de lijst.
          throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }
}
