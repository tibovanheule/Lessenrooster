package timetable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import timetable.db.DAO;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.objects.Item;
import timetable.objects.Lecture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class model, contains the lectures and items for the views, and notifies them on changes.
 */
public class MainModel implements Observable {
    private List<InvalidationListener> listenerList = new ArrayList<>();
    private ArrayList<Item> items = new ArrayList<>();
    private DataAccessProvider dataAccessProvider;
    private String standardSchedule;
    private HashMap<Integer, ArrayList<Lecture>> schedule = new HashMap<>();
    private Boolean clearText;
    private Boolean itemsChanged;
    private Boolean lecturesChanged = false;

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

    public Boolean getItemsChanged() {
        return itemsChanged;
    }

    public void setItemsChanged(Boolean itemsChanged) {
        this.itemsChanged = itemsChanged;
    }

    public Boolean getLecturesChanged() {
        return lecturesChanged;
    }

    /**
     * Function to notify all listeners of a change
     */
    private void fireInvalidationEvent() {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
        /* For debugging purposes
        System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");*/
    }

    /**
     * Function to get the schedule of a week, using a item as keyword
     */
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

    /**
     * Function to return a schedule for a given day.
     */
    public ArrayList<Lecture> getSchedule(Integer key) {
        // TODO: 28/04/2018 Find alternative to boolean lecturesChanged
        if (key.equals(5)) {
            lecturesChanged = false;
        }
        return schedule.get(key);

    }

    /**
     * Function to get a list of students, teacher, locations or courses
     */
    public void changeItems(String whatList) {
        itemsChanged = true;
        clearText = true;
        //listview leeg maken voor nieuwe items
        items.clear();
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
            HashMap<String, DAO> lists = new HashMap<>();
            lists.put("students", dac.getStudentsDAO());
            lists.put("teacher", dac.getTeacherDAO());
            lists.put("location", dac.getLocationDAO());
            lists.put("lecture", dac.getLectureDoa());

            for (Item item : lists.get(whatList).get()) {
                items.add(item);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        fireInvalidationEvent();
    }

    /**
     * Function to get a filtered list of students, teachers, locations and courses using a keyword.
     */
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
                // TODO: 28/04/2018 werk met DAO interface
                /*ArrayList<DAO> daos = new ArrayList<>();
                daos.add(dac.getLocationDAO());
                daos.add(dac.getStudentsDAO());
                daos.add(dac.getTeacherDAO());
                daos.add(dac.getLectureDoa());*/
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


    /**
     * Function to add an listener to the list.
     */
    @Override
    public void addListener(InvalidationListener listener) {
        listenerList.add(listener);
    }

    /**
     * Function to remove an listener from the list.
     */
    @Override
    public void removeListener(InvalidationListener listener) {
        listenerList.remove(listener);
    }


}
