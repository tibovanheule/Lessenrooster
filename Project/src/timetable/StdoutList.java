package timetable;

import timetable.config.Config;
import timetable.db.*;
import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Item;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class StdoutList {
    private DataAccessProvider dataAccessProvider;
    public StdoutList(String sort){
        Config config = new Config();
        Properties properties = config.getproperties();
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            dataAccessProvider = new MysqlDataAccessProvider();
        } else {
            dataAccessProvider = new SqliteDataAccessProvider();
        }

        try(BufferedWriter group = new BufferedWriter(new OutputStreamWriter(System.out))) {
            try(DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                ItemsDAO itemsDAO = dac.getItemDoa();
                for (Item item : itemsDAO.getList(sort.toLowerCase())) {
                    group.write(item.getName() + "\n");
                    group.flush();
                }
            }catch (DataAccessException e){
                System.out.println(e);
            }
        }catch (IOException e) {
            System.out.println(e);
        }
    }
}
