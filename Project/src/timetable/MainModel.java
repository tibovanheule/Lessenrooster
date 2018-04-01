package timetable;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

import java.util.ArrayList;
import java.util.List;

public class MainModel implements Observable {
    private List<InvalidationListener> listenerList = new ArrayList<>();

    private void fireInvalidationEvent() {
        for (InvalidationListener listener : listenerList) {
            listener.invalidated(this);
        }
        System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEe");
    }

    public void getList(String userData) {
        //items = database.getList();
        //for (Map.Entry<String,Item> entry: items.entrySet()) {
        //list.getItems().add(entry.getKey());
        //}
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
