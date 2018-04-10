package timetable.settings.periods;

import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import timetable.Controller;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.sqlite.SqliteDataAccessProvider;
import timetable.objects.Lecture;
import timetable.objects.Period;
import timetable.settings.SettingsController;
import timetable.views.LectureListView;

import java.util.Properties;

public class PeriodsController {
    private Controller mainController;
    private Properties properties;
    private Stage stage;
    private SettingsController settingsController;
    public TableView<Period> table;
    public TableColumn id, hour, minute;

    public void initialize() {
        id.setCellFactory(new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn myObjectListView) {
                TableCell cell = new TableCell() {
                    {
                        //gevonden fix voor de wrap text
                        prefWidthProperty().bind(this.widthProperty().subtract(20));
                    }

                    @Override
                    protected void updateItem(Period period) {
                        super.updateItem(period, b);
                        if (b || period == null) {
                            setText(null);
                            setGraphic(null);
                            this.setWrapText(true);
                        } else {
                            setText(period.getBlock()+"");
                        }
                    }

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

