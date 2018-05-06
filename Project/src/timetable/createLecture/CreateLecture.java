/*Tibo Vanheule*/
package timetable.createLecture;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.Controller;
import timetable.MainModel;
import timetable.StdError;
import timetable.comboboxes.ItemCombobox;
import timetable.comboboxes.PeriodsCombobox;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Lecture;

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
    public void setStageAndSetupListeners(Stage stage, MainModel model, Controller controller) {
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
            new StdError("Error", "Error", "Create a student, teacher or location first!", Alert.AlertType.ERROR);
            this.stage.close();
            controller.create();
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

        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            if (name.getText().trim().isEmpty()) {
                new StdError("Error", "Empty name", "Pls, add a name for the course!", Alert.AlertType.ERROR);
                throw new IllegalArgumentException("the name is empty");
            }
            Lecture newLecture = new Lecture(students.getSelectionModel().getSelectedItem().getName(),
                    teacher.getSelectionModel().getSelectedItem().getName(),
                    loc.getSelectionModel().getSelectedItem().getName(),
                    name.getText(),
                    day.getSelectionModel().getSelectedIndex() + 1,
                    period.getSelectionModel().getSelectedItem().getId(),
                    duration.getSelectionModel().getSelectedItem(),
                    period.getSelectionModel().getSelectedItem().getHour(),
                    period.getSelectionModel().getSelectedItem().getMinute(),
                    students.getSelectionModel().getSelectedItem().getId(),
                    teacher.getSelectionModel().getSelectedItem().getId(),
                    loc.getSelectionModel().getSelectedItem().getId()
            );
            try {
                if (!dac.getLectureDoa().conflict(newLecture)) {
                    dac.getLectureDoa().create(newLecture);
                    model.refresh();
                } else {
                    new StdError("Error", "Conflict", "The lesson that you are trying to create, \nhappens at the same time as another lesson", Alert.AlertType.ERROR);
                }
            } catch (NullPointerException e) {
                new StdError("Error", "Empty field", "There are empty fields, couldn't create new lecture", Alert.AlertType.ERROR);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            /*empty name don't worry*/
        }
        stage.close();

    }

    public void close() {
        if (canClose) {
            stage.close();
        }
    }
}
