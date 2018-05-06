package timetable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.control.Alert;
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
 *
 * @author Tibo Vanheule
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
    private Item currentItem;

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
    }

    /**
     * Function to refresh the schedule after an edit or deletion of a lecture
     */
    public void refresh() {
        if (currentItem != null) {
            setSchedule(currentItem);
        }
    }

    /**
     * Function to get the schedule of a week, using a item as keyword
     */
    public void setSchedule(Item selected) {
        currentItem = selected;
        lecturesChanged = true;
        if (selected != null) {
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                schedule = dac.getLectureDoa().getWeek(currentItem);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }
        fireInvalidationEvent();
    }

    /**
     * Function to return a schedule for a given day.
     */
    public ArrayList<Lecture> getSchedule(Integer key) {
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
            lists.put("course", dac.getLectureDoa());

            for (Item item : lists.get(whatList).get()) {
                items.add(item);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            new StdError("error", "Invalid config", "There is something wrong with your\nconfig file, please fix it!\ninvalid standard schedule set!", Alert.AlertType.ERROR);
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
            changeItems(standardSchedule);
        } else {
            try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
                ArrayList<DAO> daos = new ArrayList<>();
                daos.add(dac.getLocationDAO());
                daos.add(dac.getStudentsDAO());
                daos.add(dac.getTeacherDAO());
                daos.add(dac.getLectureDoa());
                for (DAO dao : daos) {
                    for (Item item : dao.getFiltered(searchText)) {
                        items.add(item);
                    }
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
