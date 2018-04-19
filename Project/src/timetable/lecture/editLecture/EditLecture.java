/*Tibo Vanheule*/
package timetable.lecture.editLecture;

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

    private Stage stage;
    private Lecture lecture;
    private LectureController controller;
    public ComboBox<Integer> block, duration;
    public ComboBox<String> students, day, loc;
    public ComboBox<Item> teacher;
    public TextField name;
    private DataAccessProvider dataAccessProvider;
    private static final String[] days = {"monday", "tuesday", "wednesday", "thursday", "friday"};

    public void setStageAndSetupListeners(Stage stage, Lecture lecture, LectureController controller, DataAccessProvider dataAccessProvider) {
        this.stage = stage;
        this.lecture = lecture;
        this.controller = controller;
        this.dataAccessProvider = dataAccessProvider;
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {
            for (Item item : dac.getStudentsDAO().getStudent()) {
                teacher.getItems().add(item);
            }
            for (Item item : dac.getTeacherDAO().getTeacher()) {
                students.getItems().add(item.getName());
            }
            for (Item item : dac.getLocationDAO().getLocation()) {
                loc.getItems().add(item.getName());
            }
            for (Period item : dac.getPeriodDAO().getPeriods()) {
                block.getItems().add(item.getBlock());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setText(lecture.getCourse());
        block.setValue(lecture.getBlock());
        duration.setValue(lecture.getDuration());/*
        teacher.setValue(lecture.getTeacher());*/
        students.setValue(lecture.getStudent());
        day.setValue(days[lecture.getDay() - 1]);
        loc.setValue(lecture.getLocation());

    }

    public void initialize() {
        day.getItems().addAll(days);
        duration.getItems().addAll(1, 2, 3);

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

    }

    public void close() {
        // TODO: 10/04/2018 onderliggende stage realtime aanpassen
        controller.canClose = true;
        try (DataAccessContext dac = dataAccessProvider.getDataAccessContext()) {

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        stage.close();
    }
}
