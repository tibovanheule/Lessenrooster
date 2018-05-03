/*Tibo Vanheule*/
package timetable.createLecture;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.MainModel;
import timetable.StdError;
import timetable.comboboxes.ItemCombobox;
import timetable.comboboxes.PeriodsCombobox;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Period;

/**
 * class to let the user create an lecture.
 *
 * @author Tibo Vanheule
 */
public class CreateLecture {
    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
    private Boolean canClose = true;
    private Stage stage;
    @FXML
    private ComboBox<Integer> duration;
    @FXML
    private PeriodsCombobox period;
    @FXML
    private ComboBox<String> day;
    @FXML
    private ItemCombobox teacher, students, loc;
    @FXML
    private TextField name;
    private MainModel model;

    /**
     * set up fields lecture, stage and a model, adds elements of database to the comboboxes, set values
     */
    public void setStageAndSetupListeners(Stage stage, MainModel model) {
        this.stage = stage;
        this.model = model;
        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            dac.getStudentsDAO().get().forEach(o -> students.getItems().add(o));
            dac.getTeacherDAO().get().forEach(o -> teacher.getItems().add(o));
            dac.getLocationDAO().get().forEach(o -> loc.getItems().add(o));
            dac.getPeriodDAO().getPeriods().forEach(o -> period.getItems().add(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            duration.setValue(duration.getItems().get(0));
            teacher.setValue(teacher.getItems().get(0));
            students.setValue(students.getItems().get(0));
            loc.setValue(loc.getItems().get(0));
            day.setValue(days[0]);
            period.setValue(period.getItems().get(0));
        } catch (Exception e) {
            // TODO: 1/05/2018 open student etc
            new StdError("Error", "Error", "Create a student, teacher or location first!", Alert.AlertType.ERROR);
            stage.close();


        }

    }

    /**
     * set up combobox day and duration
     */
    public void initialize() {
        day.getItems().addAll(days);
        duration.getItems().addAll(1, 2, 3, 4);
    }

    /**
     * close and create lecture
     */
    public void saveAndClose() {
        if (!name.getText().isEmpty()) {
            try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
                /*Objects are made to have */
                Item studentItem = students.getSelectionModel().getSelectedItem();
                Item teacherItem = teacher.getSelectionModel().getSelectedItem();
                Item locationItem = loc.getSelectionModel().getSelectedItem();
                Integer dayInt = day.getSelectionModel().getSelectedIndex() + 1;
                Integer durationInt = duration.getSelectionModel().getSelectedItem();
                Period period2 = period.getSelectionModel().getSelectedItem();
                String course = name.getText();
                Lecture newLecture = new Lecture(studentItem.getName(), teacherItem.getName(),
                        locationItem.getName(), course, dayInt, period2.getId(), durationInt, period2.getHour(),
                        period2.getMinute(), studentItem.getId(), teacherItem.getId(), locationItem.getId());


                try {
                    if (!dac.getLectureDoa().conflict(newLecture)) {
                        dac.getLectureDoa().create(newLecture);
                        model.refresh();
                    } else {
                        new StdError("Error", "Conflict", "The lesson that you are trying to create, \n would conflict with another lesson.", Alert.AlertType.ERROR);
                    }
                } catch (NullPointerException e) {
                    new StdError("Error", "Empty field", "There are empty fields, couldn't create new lecture", Alert.AlertType.ERROR);
                    close();
                }

            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            stage.close();
        } else {
            canClose = false;
            new StdError("Error", "Empty name", "Pls, add a name for the course!", Alert.AlertType.ERROR);
            canClose = true;
        }

    }

    public void close() {
        if (canClose) {
            stage.close();
        }
    }
}
