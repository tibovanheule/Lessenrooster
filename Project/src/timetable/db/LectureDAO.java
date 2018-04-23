package timetable.db;

import timetable.objects.Item;
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;

public interface LectureDAO {
    HashMap<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException;

    Iterable<Item> getLectures() throws DataAccessException;
}
