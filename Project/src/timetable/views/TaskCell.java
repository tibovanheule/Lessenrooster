package timetable.views;

import javafx.concurrent.Task;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import timetable.objects.Item;

public class TaskCell extends ListCell<Item> {

    public TaskCell() {
    }

    {
        //gevonden fix voor de wrap text
        /*enkel setWraptext(true) werkt niet (geen idee waarom, bug mss) hieronder is een gevonden workaround
         * in feite de breedte van de cell even groot maken als de Listview door die te koppellen aan elkaar (via bind) */
        prefWidthProperty().bind(this.widthProperty().subtract(20));
    }

    @Override
    protected void updateItem(Item item, boolean b) {
        super.updateItem(item, b);
        if (b || item == null) {
            setText(null);
            setGraphic(null);
            this.setWrapText(true);
        } else {
            setText(item.getName());
        }
    }

}

