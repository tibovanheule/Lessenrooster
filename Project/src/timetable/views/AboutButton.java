/*Tibo Vanheule*/
package timetable.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.Main;
import timetable.about.AboutController;

/**
 * Class extends button with custom handle event handler
 *
 * @author Tibo Vanheule
 */
public class AboutButton extends Button implements EventHandler<ActionEvent> {

    /**
     * Constructor on creation set the OnAction to this.
     */
    public AboutButton() {
        setOnAction(this);
    }

    /**
     * Override the handle action, display a new stage.
     */
    @Override
    public void handle(ActionEvent event) {
        /*functie voor het about the program weer te geven*/
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("about/about.fxml"));
            Parent root = loader.load();
            AboutController controller = loader.getController();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root, 450, 450));
            controller.setStageAndSetupListeners(stage);
            stage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/images/icon.png")));
            stage.setTitle("About");
            stage.show();
            stage.focusedProperty().addListener(o -> controller.close());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
