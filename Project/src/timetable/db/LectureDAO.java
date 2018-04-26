package timetable.db;

import timetable.objects.Item;
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;

public interface LectureDAO extends DAO {
    HashMap<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException;

    Iterable<Item> getLectures() throws DataAccessException;
    Iterable<Item> getFilteredLectures(String searchText) throws DataAccessException;
}
