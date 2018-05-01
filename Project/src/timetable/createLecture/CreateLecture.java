/*Tibo Vanheule*/
package timetable.createLecture;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import timetable.MainModel;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.comboboxes.ItemCombobox;
import timetable.comboboxes.PeriodsCombobox;
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
        try {
            duration.setValue(duration.getItems().get(0));
            teacher.setValue(teacher.getItems().get(0));
            students.setValue(students.getItems().get(0));
            loc.setValue(loc.getItems().get(0));
            day.setValue(days[0]);
            period.setValue(period.getItems().get(0));
        }catch (Exception e){
            // TODO: 1/05/2018 open student etc
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Create a student, teacher or location first!");
            alert.showAndWait();
            close();
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

            try {
                dac.getLectureDoa().create(new Lecture(studentItem.getName(), teacherItem.getName(),
                        locationItem.getName(), course, dayInt, period2.getId(), durationInt, period2.getHour(),
                        period2.getMinute(), studentItem.getId(), teacherItem.getId(), locationItem.getId()));
                model.refresh();
            }catch (Exception e ){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Empty field");
                alert.setContentText("There are empty fields, couldn't create new lecture");
                alert.showAndWait();
                close();
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        stage.close();
    }

    public void close(){
        stage.close();
    }
}
