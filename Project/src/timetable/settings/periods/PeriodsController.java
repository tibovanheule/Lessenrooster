package timetable.settings.periods;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import timetable.Controller;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Period;

public class PeriodsController {
    private Controller mainController;
    private Stage stage;
    public TableView<Period> table;
    public TableColumn<Period, Integer>  hour, minute;
    public TableColumn<Period, Boolean> delete;

    public void initialize() {

        class ButtonCell extends TableCell<Period, Boolean> {
            private final Button cellButton = new Button();

            private ButtonCell(){

                cellButton.setOnAction(new EventHandler<ActionEvent>(){

                    @Override
                    public void handle(ActionEvent t) {

                        int selectdIndex = getTableRow().getIndex();

                        //Create a new table show details of the selected item
                        Period selectedRecord = table.getItems().get(selectdIndex);
                      System.out.println(selectedRecord.getId());
                    }
                });
            }

            @Override
            protected void updateItem(Boolean t, boolean empty) {
                super.updateItem(t, empty);
                if(!empty){
                    setGraphic(cellButton);
                }
            }
        }
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


    public void setStageAndSetupListeners(Stage stage, Controller controller) {
        this.stage = stage;
        this.mainController = controller;

        try (DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()) {
            table.getItems().addAll(dac.getPeriodDAO().getPeriods());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void update(Period period, Integer hour) {
        period.setHour(hour);
        System.out.println("id: " + period.getId() + " hour: " + period.getHour() + " minute: " + period.getMinute());

        try(DataAccessContext dac = mainController.getModel().getDataAccessProvider().getDataAccessContext()){
            dac.getPeriodDAO().updatePeriods(period);
        }catch (DataAccessException e){
            e.printStackTrace();
        }
    }

    public void close() {
        // TODO: 10/04/2018 update periods realtime
        stage.close();
    }
}

