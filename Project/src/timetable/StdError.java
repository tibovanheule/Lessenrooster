package timetable;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;


/**
 * Class to output an custom error message to the standard error
 *
 * @author Tibo Vanheule
 */
public class StdError {
    /**
     * Function to output students,teachers or locations onto the standaard output
     */
    public StdError(String error) {
        try (BufferedWriter errorWriter = new BufferedWriter(new OutputStreamWriter(System.err))) {
            errorWriter.append(error);
            /*errorWriter.write(error);*/
            errorWriter.flush();

        } catch (IOException errorWhileGivingAErrorLol) {
            errorWhileGivingAErrorLol.printStackTrace();
        }
    }

    public StdError(String tilte, String head, String error, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(tilte);
        alert.setHeaderText(head);
        alert.setContentText(error);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Main.class.getResource("resources/images/icon.png").toString()));
        alert.showAndWait();
    }

}
