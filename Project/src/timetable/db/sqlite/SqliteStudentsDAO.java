package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.StudentsDAO;
import timetable.objects.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SqliteStudentsDAO extends SqliteAbstractDOA implements StudentsDAO {
    SqliteStudentsDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Iterable<Item> getStudent() throws DataAccessException {
        Iterable<Item> items = new ArrayList<>();
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery("select name from students")) {
            while (resultSet.next()) {
                ((ArrayList<Item>) items).add(new Item("students", resultSet.getString("name")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve items", e);
        }

        return items;
    }

    @Override
    public Iterable<Item> getFilteredStudent(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String selection = "SELECT * FROM students WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (PreparedStatement statement = prepare(selection)) {
            statement.setString(1, "%" + searchWord + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("students", resultSet.getString("name")));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve items", e);
        }
        return items;
    }

    @Override
    public int createStudent(String item) throws DataAccessException {
        String insert = "INSERT INTO students (id,name) VALUES (?,?)";
        try (PreparedStatement statement = prepare(insert)) {
            statement.setString(2, item);
            statement.execute();
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not create student", e);
        }
        return 0;
    }
}
