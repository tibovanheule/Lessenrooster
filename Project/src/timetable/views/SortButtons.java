package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import timetable.MainModel;

public class SortButtons extends Button implements InvalidationListener, EventHandler<ActionEvent> {

    private MainModel model;

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
        setUnderline(true);
    }

    @Override
    public void handle(ActionEvent t) {
        // TODO: 28/03/2018 geef button click dooor met de userdata
        model.getList(getUserData().toString());
        System.out.println("buttonpress registered");
    }
}