package timetable.settings.periods;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import timetable.Controller;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Period;
import timetable.settings.SettingsController;

import java.util.Properties;

public class PeriodsController {
    private Controller mainController;
    private Properties properties;
    private Stage stage;
    private SettingsController settingsController;
    public TableView<Period> table;
    public TableColumn<Period, Integer> id, hour, minute;

    public void initialize() {
        id.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Period, Integer>, ObservableValue<Integer>>() {
                                   public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Period, Integer> p) {
                                       ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getBlock()).asObject();
                                       return obsInt;
                                   }
                               }
        );
        hour.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Period, Integer>, ObservableValue<Integer>>() {
                                   public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Period, Integer> p) {
                                       ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getHour()).asObject();
                                       return obsInt;
                                   }
                               }
        );
        minute.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Period, Integer>, ObservableValue<Integer>>() {
                                   public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Period, Integer> p) {
                                       ObservableValue<Integer> obsInt = new SimpleIntegerProperty(p.getValue().getMinute()).asObject();
                                       return obsInt;
                                   }
                               }
        );
    }


    public void setStageAndSetupListeners(Stage stage, Controller controller, SettingsController settingsController, Properties properties) {
        this.stage = stage;
        this.mainController = controller;
        this.settingsController = settingsController;
        this.properties = properties;

        try (DataAccessContext dac = new SqliteDataAccessProvider().getDataAccessContext()) {
            table.getItems().addAll(dac.getPeriodDAO().getPeriods());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        // TODO: 10/04/2018 update periods
        stage.close();
    }
}

