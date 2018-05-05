//Tibo Vanheule
package timetable;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import timetable.config.Config;
import timetable.objects.Item;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Main application, checks agruments
 *
 * @author Tibo Vanheule
 */
public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Checks arguments, then acts on those...
     */
    public static void main(String[] args) {
        HashMap<Integer, Runnable> arg = new HashMap<>();
        arg.put(0, () -> launch(args));
        arg.put(2, () -> launch(args));
        arg.put(3, () -> launch(args));
        arg.put(1, () -> {
            new StdoutList(args[0]);
            Platform.exit();
            System.exit(0);
        });
        try {
            arg.get(args.length).run();
        } catch (Exception e) {
            e.printStackTrace();
            new StdError("Invalid! please don't give more than 3 arguments! :) \n");
            Platform.exit();
            System.exit(2);
        }
    }


    /**
     * Setup Stage.
     * starts program normal or loads a schedule or takes a screenschot.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Config config = new Config();
        Properties properties = config.getproperties();
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 1000, 600);
        root.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resources/images/icon.png")));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(Boolean.parseBoolean(properties.getProperty("startMaximized")));
        controller.setStageAndSetupListeners(primaryStage);

        /*Could have done this with a if-statement too, but some prefer a if-statement-less program*/
        HashMap<Integer, Runnable> args = new HashMap<>();
        args.put(0, primaryStage::show);
        args.put(2, () -> {
            loadSchedule(controller);
            primaryStage.show();
        });
        args.put(3, () -> {
            screenshot(controller, root);
            primaryStage.show();
        });
        args.get(getParameters().getRaw().size()).run();
    }

    /**
     * Function to load a schedule while te program starts
     */
    private void loadSchedule(Controller controller) {
        try {
            Runnable runnable = new Thread(() -> {
                controller.getModel().setSchedule(new Item(getParameters().getRaw().get(0), getParameters().getRaw().get(1), null));
                controller.getDraw().setVisible(false);
            });
            Platform.runLater(runnable);
        } catch (Exception e) {
            /*e.printStackTrace();*/
        }
    }

    /**
     * Function to take a screenschot of the stage
     */
    private void screenshot(Controller controller, Parent root) {
        try {
            controller.getTime().setText(getParameters().getRaw().get(1));
            controller.getDay().setVisible(false);
            controller.getDate().setVisible(false);
            controller.getModel().setSchedule(new Item(getParameters().getRaw().get(0), getParameters().getRaw().get(1), null));
            controller.getDraw().setVisible(false);
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setTransform(new Scale(2, 2));
            WritableImage image = root.snapshot(snapshotParameters, null);

            File file;
            /*controleer als met .png eindigt*/
            if (getParameters().getRaw().get(2).endsWith(".png")) {
                file = new File(getParameters().getRaw().get(2));
            } else {
                file = new File(getParameters().getRaw().get(2) + ".png");
            }
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                new StdError(e.getMessage());
                Platform.exit();
                System.exit(2);
            }
        } catch (Exception e) {
            /*e.printStackTrace();*/
            new StdError("Invalid! Please, check your arguments ,sir! They are trump-shit-arguments ;) \n");
        }
        Platform.exit();
        System.exit(0);
    }

}
