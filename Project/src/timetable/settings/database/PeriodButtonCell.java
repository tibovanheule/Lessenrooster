package timetable.settings.database;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import timetable.objects.Period;

class PeriodButtonCell extends TableCell<Period, Boolean> {
    private final Button cellButton = new Button();

    public PeriodButtonCell(TableView<Period> table, DatabaseController databaseController) {
        cellButton.setText("Delete");
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
