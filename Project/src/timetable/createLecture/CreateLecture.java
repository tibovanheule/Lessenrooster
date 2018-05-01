/*Tibo Vanheule*/
package timetable.createLecture;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.MainModel;
import timetable.createLecture.Comboboxes.PeriodsCombobox;
import timetable.createLecture.Comboboxes.ItemCombobox;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.lecture.LectureController;
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
    private Stage stage;
    private LectureController controller;
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
     * set up fields lecture stage controllermodel, add elements of database to the comboboxes, set values
     */
    public void setStageAndSetupListeners(Stage stage, LectureController controller, MainModel model) {
        this.stage = stage;
        this.controller = controller;
        this.model = model;
        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            for (Item item : dac.getStudentsDAO().get()) {
                students.getItems().add(item);
            }
            for (Item item : dac.getTeacherDAO().get()) {
                teacher.getItems().add(item);
            }
            for (Item item : dac.getLocationDAO().get()) {
                loc.getItems().add(item);
            }
            for (Period item : dac.getPeriodDAO().getPeriods()) {
                period.getItems().add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public void close() {

        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            /*Objects are made to have */
            Item studentItem = students.getSelectionModel().getSelectedItem();
            Item teacherItem = teacher.getSelectionModel().getSelectedItem();
            Item locationItem = loc.getSelectionModel().getSelectedItem();
            Integer dayInt = day.getSelectionModel().getSelectedIndex() + 1;
            Integer durationInt = duration.getSelectionModel().getSelectedItem();
            Period period2 = period.getSelectionModel().getSelectedItem();

            String course;
            if (name.getText().isEmpty()) {
                course = "Nameless lesson";
            } else {
                course = name.getText();
            }

            dac.getLectureDoa().create(new Lecture(studentItem.getName(), teacherItem.getName(),
                    locationItem.getName(), course, dayInt, period2.getId(), durationInt, period2.getHour(),
                    period2.getMinute(), studentItem.getId(), teacherItem.getId(), locationItem.getId()));
            model.refresh();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        controller.setCanClose(true);
        stage.close();
    }
}
