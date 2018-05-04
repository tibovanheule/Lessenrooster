/*Tibo Vanheule*/
package timetable.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import timetable.Main;
import timetable.MainModel;
import timetable.lecture.LectureController;
import timetable.objects.Lecture;

/**
 * @author Tibo Vanheule
 */
public class LectureListView extends ListView<Lecture> implements InvalidationListener, EventHandler<MouseEvent> {
    private MainModel model;

    public LectureListView() {

        setCellFactory(new Callback<ListView<Lecture>, ListCell<Lecture>>() {
            @Override
            public ListCell<Lecture> call(ListView<Lecture> myObjectListView) {
                ListCell<Lecture> cell = new ListCell<Lecture>() {
                    {
                        //gevonden fix voor de wrap text
                        prefWidthProperty().bind(this.widthProperty());
                        minWidthProperty().bind(this.widthProperty());
                    }

                    @Override
                    protected void updateItem(Lecture lecture, boolean b) {
                        super.updateItem(lecture, b);
                        if (b || lecture == null) {
                            setText(null);
                            setGraphic(null);
                            getStyleClass().remove("conflict");
                            setOnMouseClicked(null);
                        } else {
                            setText(lecture.getTime() + " uur \n" + lecture.getCourse());
                            if (lecture.getConflict()) {
                                getStyleClass().add("conflict");
                            }
                            setWrapText(true);
                            setOnMouseClicked(LectureListView.this::handle);
                        }
                    }

                    @Override
                    public void updateSelected(boolean selected) {
                        /*door deze methode te overschrijven slagen we er dat de stijlregels niet overschreven worden
                         * wanneer iets geselecteerd wordt. namelijk dat de text wit werd met een blauwe achtergrond*/
                    }
                };
                return cell;
            }
        });
    }

    // getter is nodig om het attribuut 'model' te kunnen gebruiken in ButtonsSeven.fxml
    public MainModel getModel() {
        return model;

    }

    public void setModel(MainModel model) {
        this.model = model;
        model.addListener(this);
    }

    /**
     * When invalidated, Clear all items in the listview and new ones
     * invokes model.getSchedule
     */
    @Override
    public void invalidated(Observable o) {

        if (model.getLecturesChanged()) {
            try {
                getItems().clear();
                getItems().addAll(model.getSchedule(Integer.parseInt(getUserData().toString())));
            } catch (NullPointerException e) {

            }
        }

    }

    /**
     * when clicked upon, open a new stage lecture. The stage displays information about the lecture.
     */
    @Override
    public void handle(MouseEvent event) {
        /*functie om de aangeklikte les weer te geven */
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("lecture/lecture.fxml"));
            Parent root = loader.load();
            LectureController controller = loader.getController();
            Stage stage = new Stage();
            controller.setStageAndSetupListeners(stage, model);
            controller.setLecture(getSelectionModel().getSelectedItem());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/images/icon.png")));
            stage.setTitle("Lecture");
            stage.setScene(new Scene(root, 450, 450));
            stage.show();
            /*stage.focusedProperty().addListener(o -> controller.close());*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
