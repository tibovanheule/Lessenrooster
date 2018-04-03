package timetable.db;

import timetable.objects.Item;
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public interface LectureDAO {
    HashMap<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException;
}
