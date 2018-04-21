package timetable.settings.periods;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    public TableColumn<Period, Integer> delete, hour, minute;

    public void initialize() {
        delete.setEditable(false);
        delete.setCellValueFactory(new PropertyValueFactory<>("block"));
        delete.setCellFactory(column -> {
            TableCell<Period, Integer> cell = new TextFieldTableCell<>();
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

        try (DataAccessContext dac = mainController.model.getDataAccessProvider().getDataAccessContext()) {
            table.getItems().addAll(dac.getPeriodDAO().getPeriods());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void update(Period period, Integer hour) {
        period.setHour(hour);
        System.out.println("id: " + period.getId() + " hour: " + period.getHour() + " minute: " + period.getMinute());

        try(DataAccessContext dac = mainController.model.getDataAccessProvider().getDataAccessContext()){
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

