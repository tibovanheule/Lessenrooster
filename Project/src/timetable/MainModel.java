package timetable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.objects.Item;
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainModel implements Observable {
    private List<InvalidationListener> listenerList = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private DataAccessProvider dataAccessProvider;
    private String standardSchedule;
    private HashMap<Integer, ArrayList<Lecture>> schedule = new HashMap<>();
    private Boolean clearText;
    private Boolean itemsChanged;
    private Boolean lecturesChanged = false;

    public MainModel() {


    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public DataAccessProvider getDataAccessProvider() {
        return dataAccessProvider;
    }

    public void setDataAccessProvider(DataAccessProvider dataAccessProvider) {
        this.dataAccessProvider = dataAccessProvider;
    }

    public String getStandardSchedule() {
        return standardSchedule;
    }

    public void setStandardSchedule(String standardSchedule) {
        this.standardSchedule = standardSchedule;
    }

    public Boolean getClearText() {
        return clearText;
    }

    public void setClearText(Boolean clearText) {
        this.clearText = clearText;
    }

    public Boolean getItemsChanged() {
        return itemsChanged;
    }

    public void setItemsChanged(Boolean itemsChanged) {
        this.itemsChanged = itemsChanged;
    }

    public Boolean getLecturesChanged() {
        return lecturesChanged;
    }

    public void setLecturesChanged(Boolean lecturesChanged) {
        this.lecturesChanged = lecturesChanged;
    }

    public void fireInvalidationEvent() {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
        /*System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");*/
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
        if (key.equals(5)) {
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
            HashMap<String, Iterable<Item>> lists = new HashMap<>();
            lists.put("students", dac.getStudentsDAO().getStudent());
            lists.put("teacher", dac.getTeacherDAO().getTeacher());
            lists.put("location", dac.getLocationDAO().getLocation());
            lists.put("lecture", dac.getLectureDoa().getLectures());

            for (Item item : lists.get(whatList)) {
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
                for (Item item : dac.getStudentsDAO().getFilteredStudent(searchText)) {
                    items.add(item);
                }
                for (Item item : dac.getTeacherDAO().getFilteredTeacher(searchText)) {
                    items.add(item);
                }
                for (Item item : dac.getLocationDAO().getFilteredLocation(searchText)) {
                    items.add(item);
                }
                for (Item item : dac.getLectureDoa().getFilteredLectures(searchText)) {
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
