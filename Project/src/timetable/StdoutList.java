package timetable;

import timetable.db.DAO;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * Class to output students,teachers or locations onto the standaard output
 *
 * @author Tibo Vanheule
 */
class StdoutList {
    /**
     * function to output students,teachers or locations onto the standaard output
     */
    StdoutList(String sort) {
        DataAccessProvider dataAccessProvider;
        dataAccessProvider = new SqliteDataAccessProvider();

        try (BufferedWriter group = new BufferedWriter(new OutputStreamWriter(System.out))) {
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                HashMap<String, DAO> sorts = new HashMap<>();
                sorts.put("students", dac.getStudentsDAO());
                sorts.put("location", dac.getLocationDAO());
                sorts.put("course", dac.getLectureDoa());
                sorts.put("teacher", dac.getTeacherDAO());
                for (Item item : sorts.get(sort).get()) {
                    group.write(item.getName() + "\n");
                    group.flush();
                }
            } catch (DataAccessException e) {
                /*e.printStackTrace();*/
                new StdError("Couldn't read the database.\n");
            } catch (NullPointerException e) {
                new StdError("Check your arguments\n");
            }
        } catch (IOException e) {
            /*e.printStackTrace();*/
            new StdError("Error while trying to output the result! \n");

        }
    }
}
