package timetable.settings.database;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import timetable.objects.Period;

/**
 * Custom TableCell, to have a delete button in side a cell
 *
 * @author Tibo Vanheule
 */
class PeriodButtonCell extends TableCell<Period, Boolean> {
    private final Button cellButton = new Button();

    public PeriodButtonCell(TableView<Period> table, DatabaseController databaseController) {
        cellButton.setText("Delete");
        cellButton.setAlignment(Pos.CENTER);
        cellButton.setOnAction((event) -> {
            int selectdIndex = getTableRow().getIndex();
            Period selectedRecord = table.getItems().get(selectdIndex);
            databaseController.delete(selectedRecord);
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
