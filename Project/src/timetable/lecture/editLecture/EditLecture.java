/*Tibo Vanheule*/
package timetable.lecture.editLecture;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.MainModel;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.lecture.LectureController;
import timetable.comboboxes.ItemCombobox;
import timetable.comboboxes.PeriodsCombobox;
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Period;

/**
 * class to let the user edit, update an lecture.
 *
 * @author Tibo Vanheule
 */
public class EditLecture {

    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
    private Stage stage;
    private Lecture lecture;
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

    // TODO: 1/05/2018 kijk voor dit te verkorten

    /**
     * set up fields lecture stage controllermodel, add elements of database to the comboboxes, set values
     */
    public void setStageAndSetupListeners(Stage stage, Lecture lecture, LectureController controller, MainModel model) {
        this.stage = stage;
        this.lecture = lecture;
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
        name.setText(lecture.getCourse());
        duration.setValue(lecture.getDuration());
        teacher.setValue(new Item("teacher", lecture.getTeacher(), lecture.getTeacherId()));
        students.setValue(new Item("student", lecture.getStudent(), lecture.getStudentId()));
        loc.setValue(new Item("location", lecture.getLocation(), lecture.getLocationId()));
        day.setValue(days[lecture.getDay() - 1]);
        period.setValue(new Period(lecture.getBlock(), lecture.getHour(), lecture.getMinute()));

    }

    /**
     * set up combobox day and duration
     */
    public void initialize() {
        day.getItems().addAll(days);
        duration.getItems().addAll(1, 2, 3, 4);


    }

    /**
     * close and save the updated lecture
     */
    public void close() {

        try (DataAccessContext dac = model.getDataAccessProvider().getDataAccessContext()) {
            /*Objects are made to have */
            Item studentItem = students.getSelectionModel().getSelectedItem();
            Item teacherItem = teacher.getSelectionModel().getSelectedItem();
            Item locationItem = loc.getSelectionModel().getSelectedItem();
            String course;
            if (name.getText().isEmpty()) {
                course = "Nameless lesson";
            } else {
                course = name.getText();
            }
            Integer dayInt = day.getSelectionModel().getSelectedIndex() + 1;
            Integer durationInt = duration.getSelectionModel().getSelectedItem();
            Period period2 = period.getSelectionModel().getSelectedItem();


            dac.getLectureDoa().update(new Lecture(studentItem.getName(), teacherItem.getName(),
                    locationItem.getName(), course, dayInt, period2.getId(), durationInt, period2.getHour(),
                    period2.getMinute(), studentItem.getId(), teacherItem.getId(), locationItem.getId()), lecture);
            model.refresh();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        stage.close();
        controller.setCanClose(true);
    }
}
