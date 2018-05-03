package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.LocationDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * class implements LocationDAO, to get edit add locations in sqlite
 *
 * @author Tibo Vanheule
 */
public class SqliteLocationDAO extends SqliteAbstractDOA implements LocationDAO {
    SqliteLocationDAO(Connection connection) {
        super(connection);
    }

    /**
     * get all locations
     */
    @Override
    public Iterable<Item> get() throws DataAccessException {
        Iterable<Item> items = new ArrayList<>();
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery("select id,name from location")) {
            while (resultSet.next()) {
                ((ArrayList<Item>) items).add(new Item("location", resultSet.getString("name"), resultSet.getInt("id")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve locations", e);
        }

        return items;
    }

    /**
     * get a filterd iterable of loactions based on a keyword
     */
    @Override
    public Iterable<Item> getFiltered(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String selection = "SELECT id,name FROM location WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (PreparedStatement statement = prepare(selection)) {
            statement.setString(1, "%" + searchWord + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("location", resultSet.getString("name"), resultSet.getInt("id")));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve location", e);
        }
        return items;

    }

    /**
     * delete a location and lectures connected to that location
     */
    @Override
    public int delete(Item item) throws DataAccessException {
        String delete = "DELETE FROM location WHERE id = ?";
        String lectures = "DELETE FROM lecture WHERE location_id= ?";

        try (PreparedStatement statement2 = prepare(lectures); PreparedStatement statement = prepare(delete)) {
            statement2.setInt(1, item.getId());
            statement2.execute();
            statement.setInt(1, item.getId());
            statement.execute();
        } catch (Exception e) {
            throw new DataAccessException("could not delete student", e);
        }
        return 0;
    }

    /**
     * add a location
     */
    @Override
    public Item create(String item) throws DataAccessException {
        String insert = "INSERT INTO location (id,name) VALUES (?,?)";
        Item returnItem = null;
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(2, item);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    returnItem = new Item("location", "location", generatedKeys.getInt(1));
                }
            } catch (Exception e) {
                throw new DataAccessException("could not get inserted id", e);
            }
        } catch (Exception e) {
            throw new DataAccessException("could not create student", e);
        }
        return returnItem;
    }

    /**
     * update the name of a location
     */
    @Override
    public int updateName(Item item) throws DataAccessException {
        String insert = "UPDATE location SET name=? WHERE id=?";
        Item returnItem = null;
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("could not create location", e);
        }
        return 0;
    }


    /**
     * to check if a name isn't already in database
     */
    @Override
    public Boolean nameExists(String name) throws DataAccessException {
        name = name.replace(" ", "");
        String search = "SELECT name FROM location WHERE name=? LIMIT 1";
        try (PreparedStatement statement = prepare(search)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            throw new DataAccessException("could not create lecture", e);
        }
        return false;
    }
}
