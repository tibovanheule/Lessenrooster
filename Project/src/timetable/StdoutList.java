package timetable;

import timetable.config.Config;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Properties;

class StdoutList {
    StdoutList(String sort) {
        DataAccessProvider dataAccessProvider;
        Config config = new Config();
        Properties properties = config.getproperties();
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            /*dataAccessProvider = new MysqlDataAccessProvider();*/
            dataAccessProvider = null;
        } else {
            dataAccessProvider = new SqliteDataAccessProvider();
        }

        try (BufferedWriter group = new BufferedWriter(new OutputStreamWriter(System.out))) {
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                HashMap<String, Iterable<Item>> sorts = new HashMap<>();
                sorts.put("students", dac.getStudentsDAO().getStudent());
                sorts.put("location", dac.getLocationDAO().getLocation());
                sorts.put("course", dac.getLectureDoa().getLectures());
                sorts.put("teacher", dac.getTeacherDAO().getTeacher());
                for (Item item : sorts.get(sort)) {
                    group.write(item.getName() + "\n");
                    group.flush();
                }
            } catch (DataAccessException e) {
                /*e.printStackTrace();*/
                try (BufferedWriter error = new BufferedWriter(new OutputStreamWriter(System.err))) {
                    error.write("Couldn't read the database.\n");
                    error.flush();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            } catch (NullPointerException e) {
                try (BufferedWriter error = new BufferedWriter(new OutputStreamWriter(System.err))) {
                    error.write("Check your arguments\n");
                    error.flush();
                } catch (IOException error) {
                    error.printStackTrace();
                }
            }
        } catch (IOException e) {
            /*e.printStackTrace();*/
            try (BufferedWriter error = new BufferedWriter(new OutputStreamWriter(System.err))) {
                error.write("Error while trying to write down the result! :o \n");
                error.flush();
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }
}
