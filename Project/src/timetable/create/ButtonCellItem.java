package timetable.create;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import timetable.objects.Item;

class ButtonCellItem extends TableCell<Item, Boolean> {
    private final Button cellButton = new Button();

    ButtonCellItem(TableView<Item> table, CreateController createController) {
        cellButton.setText("Delete");
        cellButton.setOnAction((event) -> {
            int selectdIndex = getTableRow().getIndex();
            Item selectedRecord = table.getItems().get(selectdIndex);
            createController.delete(selectedRecord);
        });
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
        }
    }
}
