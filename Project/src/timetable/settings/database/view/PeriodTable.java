package timetable.settings.database.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Period;


public class PeriodTable extends TableView<Period> {
    @FXML
    private TableColumn<Period, Integer> hour, minute;
    @FXML
    private TableColumn<Period, Boolean> delete;

    class ButtonCell extends javafx.scene.control.TableCell<Period, Boolean> {
        private final Button cellButton = new Button();

        private ButtonCell() {
            cellButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    int selectdIndex = getTableRow().getIndex();

                    //Create a new table show details of the selected item
                    Period selectedRecord = getItems().get(selectdIndex);
                    System.out.println(selectedRecord.getId());
                }
            });
        }

        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                setGraphic(cellButton);
            }
        }
    }

    public PeriodTable() {
        delete.setCellFactory(column -> {
            ButtonCell cell = new ButtonCell();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        hour.setCellValueFactory(new PropertyValueFactory<>("hour"));
        hour.setCellFactory(column -> {
            TableCell<Period, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        hour.setOnEditCommit(event -> update(event.getRowValue(), event.getNewValue()));
        minute.setCellValueFactory(new PropertyValueFactory<>("minute"));
        minute.setCellFactory(column -> {
            TableCell<Period, Integer> cell = new TextFieldTableCell<>();
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
    }

    private void update(Period period, Integer hour) {
        period.setHour(hour);
        System.out.println("id: " + period.getId() + " hour: " + period.getHour() + " minute: " + period.getMinute());

        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().updatePeriods(period);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
