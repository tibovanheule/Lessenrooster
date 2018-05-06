package timetable.settings.database.table;

import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import timetable.Controller;
import timetable.StdError;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Period;

import java.util.HashMap;

/**
 * Custom table column, cellfactory
 *
 * @author Tibo Vanheule
 */
public class CustomTableColumn extends TableColumn<Period, Integer> {
    private Controller controller;
    private Integer limit = 24;

    /**
     * constructor, sets cellfactory and also makes a new StringConverter that checks if string is a number and smaller than e given limit
     */
    public CustomTableColumn() {
        StringConverter<Integer> checker = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                try {
                    return object.toString();
                }catch (NullPointerException e){
                    return null;
                }
            }

            @Override
            public Integer fromString(String string) {
                try {
                    Integer integer = Integer.parseInt(string);
                    if (integer < limit) {
                        return integer;
                    } else {
                        new StdError("Error", "Number to big", "You typed a too big number! >60", Alert.AlertType.ERROR);
                        return 0;
                    }
                } catch (NumberFormatException e) {
                    new StdError("Error", "not a number!", "You haven't typed a number!", Alert.AlertType.ERROR);
                    return 0;
                }
            }
        };
        setCellFactory(column -> new TextFieldTableCell<Period, Integer>(checker));


    }

    /**
     * sets propertyValue factory and field controller
     */
    public void setup(Controller controller, String property, Integer limit) {
        this.controller = controller;
        setCellValueFactory(new PropertyValueFactory<>(property));
        this.limit = limit;
        HashMap<String,Runnable> editsCommits = new HashMap<>();
        editsCommits.put("hour",() -> setOnEditCommit(event -> update(event.getRowValue(), event.getNewValue(), event.getRowValue().getMinute())));
        editsCommits.put("minute",() -> setOnEditCommit(event -> update(event.getRowValue(), event.getRowValue().getHour(), event.getNewValue())));
        editsCommits.get(property).run();

    }

    /**
     * updates a period
     */
    private void update(Period period, Integer hour, Integer minute) {
        period.setHour(hour);
        period.setMinute(minute);
        try (DataAccessContext dac = controller.getModel().getDataAccessProvider().getDataAccessContext()) {
            dac.getPeriodDAO().updatePeriods(period);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
