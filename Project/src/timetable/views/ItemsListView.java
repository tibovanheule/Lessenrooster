/*Tibo Vanheule*/
package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import timetable.MainModel;
import timetable.objects.Item;

/**
 * Class extends Listview
 */
public class ItemsListView extends ListView<Item> implements InvalidationListener, EventHandler<MouseEvent> {

    private MainModel model;

    public ItemsListView() {
        setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> myObjectListView) {
                return new ListCell<Item>() {
                    {
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
                };
            }
        });

        setOnMouseClicked(this);
    }

    // getter is nodig om het attribuut 'model' te kunnen gebruiken in ButtonsSeven.fxml
    public MainModel getModel() {
        return model;

    }

    public void setModel(MainModel model) {
        this.model = model;
        model.addListener(this);
        getItems().addAll(model.getItems());

    }

    @Override
    public void invalidated(Observable o) {
        if (model.getItemsChanged()) {
            getItems().clear();
            getItems().addAll(model.getItems());
            model.setItemsChanged(false);
        }
    }

    @Override
    public void handle(MouseEvent event) {
        model.setSchedule(getSelectionModel().getSelectedItem());
    }
}
