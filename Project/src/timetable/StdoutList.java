package timetable;

import timetable.config.Config;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.db.ItemsDAO;
import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

class StdoutList {
    StdoutList(String sort) {
        DataAccessProvider dataAccessProvider;
        Config config = new Config();
        Properties properties = config.getproperties();
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            dataAccessProvider = new MysqlDataAccessProvider();
        } else {
            dataAccessProvider = new SqliteDataAccessProvider();
        }

        try (BufferedWriter group = new BufferedWriter(new OutputStreamWriter(System.out))) {
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                ItemsDAO itemsDAO = dac.getItemDoa();
                for (Item item : itemsDAO.getList(sort.toLowerCase())) {
                    group.write(item.getName() + "\n");
                    group.flush();
                }
            } catch (DataAccessException e) {
                /*e.printStackTrace();*/
                try (BufferedWriter error = new BufferedWriter(new OutputStreamWriter(System.err))) {
                    error.write("Invalid! Check you're arguments, sir! :( \n");
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
