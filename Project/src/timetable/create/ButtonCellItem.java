/*Tibo Vanheule*/
package timetable.create;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import timetable.objects.Item;

/**
 * Class to display a button and when clicked upon delete the row in the table
 *
 * @author Tibo Vanheule
 */
class ButtonCellItem extends TableCell<Item, Boolean> {
    private final Button cellButton = new Button();

    /**
     * Constructor, sets the text and OnAction
     */
    ButtonCellItem(TableView<Item> table, CreateController createController) {
        cellButton.setText("Delete");
        cellButton.setOnAction((event) -> delete(table, createController));
        cellButton.setAlignment(Pos.CENTER);
    }

    @Override
    protected void updateItem(Boolean t, boolean empty) {
        super.updateItem(t, empty);
        if (empty || t == null) {
            setText(null);
            setGraphic(null);
            setOnMouseClicked(null);
        }
        if (!empty) {
            setGraphic(cellButton);
            setAlignment(Pos.CENTER);
        }
    }

    /**
     * Delete row when button is clicked
     */
    private void delete(TableView<Item> table, CreateController createController) {
        int selectedIndex = getTableRow().getIndex();
        Item selectedRecord = table.getItems().get(selectedIndex);
        createController.delete(selectedRecord);
    }
}
