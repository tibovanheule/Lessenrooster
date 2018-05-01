package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.StudentsDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Class to add, delete or update students in a sqlite database
 * @author Tibo Vanheule*/
public class SqliteStudentsDAO extends SqliteAbstractDOA implements StudentsDAO {
    SqliteStudentsDAO(Connection connection) {
        super(connection);
    }

    /**
     * gets all students*/
    @Override
    public Iterable<Item> get() throws DataAccessException {
        Iterable<Item> items = new ArrayList<>();
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery("select id,name from students")) {
            while (resultSet.next()) {
                ((ArrayList<Item>) items).add(new Item("students", resultSet.getString("name"), resultSet.getInt("id")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve items", e);
        }

        return items;
    }

    /**
     * gets a filtered iterable based on a keyword*/
    @Override
    public Iterable<Item> getFiltered(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String selection = "SELECT * FROM students WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (PreparedStatement statement = prepare(selection)) {
            statement.setString(1, "%" + searchWord + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("students", resultSet.getString("name"), resultSet.getInt("id")));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }

    /**
     * Create a student*/
    @Override
    public Item create(String item) throws DataAccessException {
        String insert = "INSERT INTO students (id,name) VALUES (?,?)";
        Item returnItem = null;
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(2, item);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    returnItem = new Item("student", "student", generatedKeys.getInt(1));
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
     * delete a student*/
    @Override
    public int delete(Item item) throws DataAccessException {
        String delete = "DELETE FROM students WHERE id = ?";
        String lectures = "DELETE FROM lecture WHERE students_id = ?";

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
     * update the name of a student*/
    @Override
    public int updateName(Item item) throws DataAccessException {
        String insert = "UPDATE students SET name=? WHERE id=?";
        Item returnItem = null;
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException("could not create student", e);
        }
        return 0;
    }


}
