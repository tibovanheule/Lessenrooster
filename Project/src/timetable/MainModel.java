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
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class MainModel implements Observable {
    private List<InvalidationListener> listenerList = new ArrayList<>();
    public ArrayList<Item> items = new ArrayList<>();
    public DataAccessProvider dataAccessProvider;
    public String standardSchedule;
    public HashMap<Integer, ArrayList<Lecture>> schedule = new HashMap<>();
    public Boolean clearText;
    public Boolean itemsChanged;
    public Boolean lecturesChanged = false;

    public MainModel() {


    }


    private void fireInvalidationEvent() {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
        System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
    }

    public void setSchedule(Item selected) {
        lecturesChanged = true;
        try {
            if (selected != null) {
                try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                    schedule = dac.getLectureDoa().getWeek(selected);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            /*e.printStackTrace();*/
        }
        fireInvalidationEvent();
    }

    public ArrayList<Lecture> getSchedule(Integer key) {
        if(key.equals(5)){
            lecturesChanged = false;
        }
        return schedule.get(key);

    }

    public void changeItems(String whatList) {
        itemsChanged = true;
        clearText = true;
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

    public void filterItems(String searchText) {
        itemsChanged = true;
        clearText = false;
        items.clear();
        if (searchText.isEmpty()) {
            //als textfield leeg is keer dan terug naar de standaard lijst
            changeItems(standardSchedule);
        } else {
            // zo niet haal de gefilterde lijst op
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                ItemsDAO itemsDAO = dac.getItemDoa();
                for (Item item : itemsDAO.getFilterdList(searchText)) {
                    items.add(item);
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
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
