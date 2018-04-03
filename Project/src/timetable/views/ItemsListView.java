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

public class ItemsListView extends ListView<Item> implements InvalidationListener, EventHandler<MouseEvent> {

    public ItemsListView() {
        setCellFactory(new Callback<>() {
            @Override
            public ListCell<Item> call(ListView<Item> myObjectListView) {
                ListCell<Item> cell = new ListCell<>() {
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
                };
                return cell;
            }
        });

        setOnMouseClicked(this::handle);
    }

    private MainModel model;

    // getter is nodig om het attribuut 'model' te kunnen gebruiken in ButtonsSeven.fxml
    public MainModel getModel() {
        return model;

    }

    public void setModel(MainModel model) {
        this.model = model;
        model.addListener(this);
        getItems().addAll(model.items);
        /*getSelectionModel().selectedItemProperty().addListener(o -> Platform.runLater(() ->model.getSchedule()));*/
    }

    @Override
    public void invalidated(Observable o) {
        if(model.itemsChanged) {
            getItems().clear();
            getItems().addAll(model.items);
            model.itemsChanged = false;
        }
    }

    @Override
    public void handle(MouseEvent event) {
        model.getSchedule(getSelectionModel().getSelectedItem());
    }
}
