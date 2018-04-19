package timetable.db.sqlite;

import timetable.db.DataAccessException;
import timetable.db.ItemsDAO;
import timetable.db.StudentsDAO;
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
    public Iterable<Item> getTeacher() throws DataAccessException {
        Iterable<Item> items = new ArrayList<>();
        try (Statement statement = create(); ResultSet resultSet = statement.executeQuery("select name from teacher")) {
            while (resultSet.next()) {
                ((ArrayList<Item>) items).add(new Item("teacher", resultSet.getString("name")));
            }
        } catch (Exception e) {
            //foutmelding weergeven in de lijst.
            throw new DataAccessException("could not retrieve teacher", e);
        }

        return items;
    }

    @Override
    public Iterable<Item> getFilteredTeacher(String searchWord) throws DataAccessException {
        ArrayList<Item> items = new ArrayList<Item>();
        String selection = "SELECT * FROM teacher WHERE name LIKE ?";
        //https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
        try (PreparedStatement statement = prepare(selection)) {
            statement.setString(1, "%" + searchWord + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(new Item("teacher", resultSet.getString("name")));
            }
            resultSet.close();
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve teacher", e);
        }
        return items;

    }
}
