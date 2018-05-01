package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.TeacherDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteTeacherDAO extends SqliteAbstractDOA implements TeacherDAO {
    SqliteTeacherDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Iterable<Item> get() throws DataAccessException {
        Iterable<Item> items = new ArrayList<>();
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery("select id,name from teacher")) {
            while (resultSet.next()) {
                ((ArrayList<Item>) items).add(new Item("teacher", resultSet.getString("name"), resultSet.getInt("id")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve teacher", e);
        }

        return items;
    }

    @Override
    public Iterable<Item> getFiltered(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String selection = "SELECT * FROM teacher WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (PreparedStatement statement = prepare(selection)) {
            statement.setString(1, "%" + searchWord + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("teacher", resultSet.getString("name"), resultSet.getInt("id")));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve teacher", e);
        }
        return items;

    }

    @Override
    public Item create(String item) throws DataAccessException {
        String insert = "INSERT INTO teacher (id,name) VALUES (?,?)";
        Item returnItem = null;
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(2, item);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    returnItem = new Item("teacher", "teacher", generatedKeys.getInt(1));
                }
            } catch (Exception e) {
                throw new DataAccessException("could not get inserted id", e);
            }
        } catch (Exception e) {
            throw new DataAccessException("could not create student", e);
        }
        return returnItem;
    }

    @Override
    public int delete(Item item) throws DataAccessException {
        String delete = "DELETE FROM teacher WHERE id = ?";
        String lectures = "DELETE FROM lecture WHERE teacher_id = ?";

        try (PreparedStatement statement2 = prepare(lectures); PreparedStatement statement = prepare(delete)) {
            statement2.setInt(1, item.getId());
            statement2.execute();
            statement.setInt(1, item.getId());
            statement.execute();
        } catch (Exception e) {
            throw new DataAccessException("could not delete teacher", e);
        }
        return 0;
    }

    @Override
    public int updateName(Item item) throws DataAccessException {
        String insert = "UPDATE teacher SET name=? WHERE id=?";
        Item returnItem = null;
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("could not create teacher", e);
        }
        return 0;
    }
}
