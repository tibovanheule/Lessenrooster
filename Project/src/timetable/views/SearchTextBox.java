package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import timetable.MainModel;

public class SearchTextBox extends TextField implements InvalidationListener, EventHandler<KeyEvent> {

    private MainModel model;

    public SearchTextBox() {
        setOnKeyTyped(this::handle);
    }

    // getter is nodig om het attribuut 'model' te kunnen gebruiken in ButtonsSeven.fxml
    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
        model.addListener(this);
    }

    @Override
    public void invalidated(Observable o) {
        if (model.getClearText()) {
            try {
                clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(KeyEvent event) {
        model.filterItems(getText());
    }
}
