/*Tibo Vanheule*/
package timetable.lecture.editLecture;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.db.DataAccessProvider;
import timetable.lecture.LectureController;
import timetable.objects.Item;
import timetable.objects.Lecture;
import timetable.objects.Period;

public class EditLecture {

    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};
    private Stage stage;
    private Lecture lecture;
    private LectureController controller;
    @FXML
    private ComboBox<Integer> duration;
    @FXML
    private ComboBox<Period> id;
    @FXML
    private ComboBox<String> students, day, loc;
    @FXML
    private ComboBox<Item> teacher;
    @FXML
    private TextField name;
    private DataAccessProvider dataAccessProvider;

    public void setStageAndSetupListeners(Stage stage, Lecture lecture, LectureController controller, DataAccessProvider dataAccessProvider) {
        this.stage = stage;
        this.lecture = lecture;
        this.controller = controller;
        this.dataAccessProvider = dataAccessProvider;
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
            for (Item item : dac.getStudentsDAO().get()) {
                teacher.getItems().add(item);
            }
            for (Item item : dac.getTeacherDAO().get()) {
                students.getItems().add(item.getName());
            }
            for (Item item : dac.getLocationDAO().get()) {
                loc.getItems().add(item.getName());
            }
            for (Period item : dac.getPeriodDAO().getPeriods()) {
                id.getItems().add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText(lecture.getCourse());
        id.setValue(new Period(lecture.getBlock(), 0, 0));
        duration.setValue(lecture.getDuration());
        teacher.setValue(new Item("teacher", lecture.getTeacher(), null));
        students.setValue(lecture.getStudent());
        day.setValue(days[lecture.getDay() - 1]);
        loc.setValue(lecture.getLocation());
        teacher.setCellFactory(new Callback<ListView<Item>, ListCell<Item>>() {
            @Override
            public ListCell<Item> call(ListView<Item> p) {
                return new ListCell<Item>() {
                    @Override
                    protected void updateItem(Item item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                };
            }
        });
        id.setCellFactory(new Callback<ListView<Period>, ListCell<Period>>() {
            @Override
            public ListCell<Period> call(ListView<Period> p) {
                return new ListCell<Period>() {
                    @Override
                    protected void updateItem(Period item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getHour() + ":" + item.getMinute());
                        }
                    }
                };
            }
        });

    }

    public void initialize() {
        day.getItems().addAll(days);
        duration.getItems().addAll(1, 2, 3);


    }

    public void close() {
        // TODO: 10/04/2018 onderliggende stage realtime aanpassen
        controller.setCanClose(true);
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        stage.close();
    }
}
