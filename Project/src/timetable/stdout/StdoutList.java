package timetable.stdout;

import timetable.config.Config;
import timetable.db.Db;
import timetable.db.Mysql;
import timetable.db.Sqlite;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;

public class StdoutList {
    private Db database;
    public StdoutList(String sort){
        Config config = new Config();
        Properties properties = config.getproperties();
        //als de property true is gebruik dan mysql
        if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
            database = new Db(new Mysql());
        } else {
            //in elk ander geval, valt het terug op Sqlite
            database = new Db(new Sqlite());
        }

        try(BufferedWriter group = new BufferedWriter(new OutputStreamWriter(System.out))) {
            for(String string:database.getList(sort.toLowerCase()).keySet()) {
                group.write(string + "\n");
                group.flush();
            }
        }catch (IOException e) {
            System.out.println(e);
        }
    }
}
