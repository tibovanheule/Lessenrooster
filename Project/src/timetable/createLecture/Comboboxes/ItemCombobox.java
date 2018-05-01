package timetable.createLecture.Comboboxes;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import timetable.objects.Item;

/**
 * custom Combobox, with cell factory.
 *
 * @author Tibo Vanheule
 */
public class ItemCombobox extends ComboBox<Item> {

    public ItemCombobox() {
        setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> p) {
                return new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
    }
}
