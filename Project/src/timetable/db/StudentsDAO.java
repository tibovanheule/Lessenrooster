package timetable.db;

import timetable.objects.Item;

public interface StudentsDAO {
    Iterable<Item> getStudent() throws DataAccessException;

    Iterable<Item> getFilteredStudent(String SearchWord) throws DataAccessException;

    int createStudent(String item) throws DataAccessException;
}
