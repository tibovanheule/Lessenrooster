/*Tibo Vanheule*/
package timetable.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import timetable.MainModel;

/**
 * Class Sortbuttons extends Button. decides what the ItemsListView views
 *
 * @author Tibo Vanheule
 */
public class SortButtons extends Button implements EventHandler<ActionEvent> {

    private MainModel model;

    /**
     * Constructor sets the Onaction event handler
     */
    public SortButtons() {
        setOnAction(this);
    }


    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    /**
     * invokes the model.changeitems method and as parameter it uses the userdata of the button
     */
    @Override
    public void handle(ActionEvent t) {
        model.changeItems(getUserData().toString());
    }
}
