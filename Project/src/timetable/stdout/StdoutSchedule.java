package timetable.stdout;

import timetable.config.Config;
import timetable.db.Db;
import timetable.db.Mysql;
import timetable.db.Sqlite;
import timetable.objects.Lecture;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class StdoutSchedule {
    private Db database;
    public StdoutSchedule(String sort, String filter) {
        Config config = new Config();
        Properties properties = config.getproperties();
        //als de property true is gebruik dan mysql
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            database = new Db(new Mysql());
        } else {
            //in elk ander geval, valt het terug op Sqlite
            database = new Db(new Sqlite());
        }
        HashMap<Integer, String> days = new HashMap<>();
        days.put(1, "monday");
        days.put(2, "tuesday");
        days.put(3, "wednesday");
        days.put(4, "thursday");
        days.put(5, "friday");

        try (BufferedWriter scheduleEntry = new BufferedWriter(new OutputStreamWriter(System.err))){
            for(Map.Entry<Integer, ArrayList<Lecture>> entry:database.getRooster(sort.toLowerCase(),filter).entrySet()) {
                for (Lecture lecture : entry.getValue()) {
                    scheduleEntry.write(days.get(lecture.getDay()) + " " + lecture.getBlock() + " "+ lecture.getCourse() + "@" + lecture.getLocation() + "\n");
                    scheduleEntry.flush();
                }
            }
        }catch (IOException e) {
            System.out.println(e);
        }
    }
}
