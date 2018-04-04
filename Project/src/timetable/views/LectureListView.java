package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import timetable.Main;
import timetable.MainModel;
import timetable.lecture.LectureController;
import timetable.objects.Lecture;

public class LectureListView extends ListView<Lecture> implements InvalidationListener, EventHandler<MouseEvent> {
    public LectureListView() {
        setOnMouseClicked(this::handle);
        setCellFactory(new Callback<ListView<Lecture>, ListCell<Lecture>>() {
            @Override
            public ListCell<Lecture> call(ListView<Lecture> myObjectListView) {
                ListCell<Lecture> cell = new ListCell<Lecture>() {
                    {
                        //gevonden fix voor de wrap text
                        prefWidthProperty().bind(this.widthProperty().subtract(20));
                    }

                    @Override
                    protected void updateItem(Lecture lecture, boolean b) {
                        super.updateItem(lecture, b);
                        if (b || lecture == null) {
                            setText(null);
                            setGraphic(null);
                            getStyleClass().remove("conflict");
                            this.setWrapText(true);
                        } else {
                            setText(lecture.getCourse() + " Uur: " + lecture.getBlock());
                            if (lecture.getConflict()) {
                                getStyleClass().add("conflict");
                            }
                        }
                    }
                };
                return cell;
            }
        });
    }

    private MainModel model;

    // getter is nodig om het attribuut 'model' te kunnen gebruiken in ButtonsSeven.fxml
    public MainModel getModel() {
        return model;

    }

    public void setModel(MainModel model) {
        this.model = model;
        model.addListener(this);
        /*getSelectionModel().selectedItemProperty().addListener(o -> Platform.runLater(() ->model.setSchedule()));*/
    }

    @Override
    public void invalidated(Observable o) {

        if (model.lecturesChanged) {
            getItems().clear();
            getItems().addAll(model.getSchedule(Integer.parseInt(getUserData().toString())));
        }

    }

    @Override
    public void handle(MouseEvent event) {
        /*functie om de aangeklikte les weer te geven */
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("lecture/lecture.fxml"));
            Parent root = loader.load();
            LectureController controller = loader.getController();
            controller.setLecture(getSelectionModel().getSelectedItem());
            Stage stage = new Stage();
            controller.setStageAndSetupListeners(stage);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
