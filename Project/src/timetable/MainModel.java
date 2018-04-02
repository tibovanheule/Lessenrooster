package timetable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.image.Image;
import timetable.config.Config;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.db.ItemsDAO;
import timetable.db.mysql.MysqlDataAccessProvider;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainModel implements Observable {
    private List<InvalidationListener> listenerList = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    private DataAccessProvider dataAccessProvider;

public MainModel(){
    Config config = new Config();
    Properties properties = config.getproperties();


    //als de property true is gebruik dan mysql
    if (Boolean.parseBoolean(properties.getProperty("DB.use"))) {
        dataAccessProvider = new MysqlDataAccessProvider();
        //deze afbeelding is voor het gemak dan weten we op welke DB we draaien als we het prog draaien
        Image image = new Image(getClass().getResourceAsStream("resources/images/mysql.png"));
        /*dbLogo.setImage(image);*/
    } else {
        //in elk ander geval, valt het terug op sqlite
        dataAccessProvider = new SqliteDataAccessProvider();
        Image image = new Image(getClass().getResourceAsStream("resources/images/sqlite.png"));
        /*dbLogo.setImage(image);*/
    }
    fireInvalidationEvent();
}


    private void fireInvalidationEvent() {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
        System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
    }

    public void changeItems(String whatList) {
        // TODO: 2/04/2018 clear textbox
        //listview leeg maken voor nieuwe items
        items.clear();
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
            ItemsDAO itemsDAO = dac.getItemDoa();
            for (Item item : itemsDAO.getList(whatList)) {
                items.add(item);
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        fireInvalidationEvent();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        listenerList.remove(listener);
    }


}
