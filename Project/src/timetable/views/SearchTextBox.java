/*Tibo Vanheule*/
package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import timetable.MainModel;

/**
 * Class extends Textfield. when text is typed get a filtered list for ItemsListview
 *
 * @author Tibo Vanheule
 */
public class SearchTextBox extends TextField implements InvalidationListener, EventHandler<KeyEvent> {

    private MainModel model;

    /**
     * Constructor, set the the event handler OnKeyTyped
     */
    public SearchTextBox() {
        setOnKeyTyped(this::handle);
    }

    public MainModel getModel() {
        return model;
    }

    public void setModel(MainModel model) {
        this.model = model;
        model.addListener(this);
    }

    /**
     * if invalidated clear the text in the text box (When a sortbutton has been clicked)
     */
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

    /**
     * invokes model.filterItems as parameter his text
     */
    @Override
    public void handle(KeyEvent event) {
        model.filterItems(getText());
    }
}
