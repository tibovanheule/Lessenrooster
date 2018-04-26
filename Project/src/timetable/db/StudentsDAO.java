package timetable.db;

import timetable.objects.Item;

public interface StudentsDAO extends DAO{
    Iterable<Item> getStudent() throws DataAccessException;

    Iterable<Item> getFilteredStudent(String SearchWord) throws DataAccessException;

    Item createStudent(String item) throws DataAccessException;

    int deleteStudent(Item item) throws DataAccessException;
}
