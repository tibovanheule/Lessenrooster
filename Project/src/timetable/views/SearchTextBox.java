package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import timetable.MainModel;

public class SearchTextBox extends TextField implements InvalidationListener, EventHandler<ActionEvent> {

    public SearchTextBox(){
        textProperty().addListener(o -> {if(!getText().equals("")){model.filterItems(getText());}});
    }

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
        if(model.clearText){
            try{
                clear();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handle(ActionEvent event) {
    }
}
