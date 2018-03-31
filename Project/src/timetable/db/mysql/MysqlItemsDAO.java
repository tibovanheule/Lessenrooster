package timetable.db.mysql;

import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class MysqlItemsDAO extends MysqlAbstractDOA implements ItemsDAO {
    public MysqlItemsDAO(Connection connection){
        super(connection);
    }

    @Override
    public Iterable<Item> getList(String sort) throws DataAccessException {
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

    @Override
    public Iterable<Item> getFilterdList(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String[] tables = {"teacher","students","location"};
        for (String table:tables) {
            String selection = "SELECT * FROM "+ table +" WHERE name LIKE ?";
            //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
            try (PreparedStatement statement = prepare(selection);) {
                statement.setString(1, "%" + searchWord + "%");
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    items.add(new Item(table, resultSet.getString("name")));
                }
                resultSet.close();
            } catch (Exception e) {
                throw new DataAccessException("could not retrieve items", e);
            }
        }
        return items;
    }
}
