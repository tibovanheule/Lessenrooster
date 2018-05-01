package timetable.db;

import timetable.objects.Item;
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;

public interface LectureDAO extends DAO {
    HashMap<Integer, ArrayList<Lecture>> getWeek(Item item) throws DataAccessException;

    Iterable<Item> get() throws DataAccessException;

    int update(Lecture lecture, Lecture old) throws DataAccessException;

    int create(Lecture lecture) throws DataAccessException;

    int delete(Lecture lecture) throws DataAccessException;
}
