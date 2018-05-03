package timetable.settings.database.table;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import timetable.Controller;
import timetable.StdError;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Period;

/**
 * Custom table column, cellfactory
 *
 * @author Tibo Vanheule
 */
public class CustomTableColumn extends TableColumn<Period, Integer> {
    private Controller controller;

    /**
     * constructor, sets cellfactory and on edit
     */
    public CustomTableColumn() {
        StringConverter<Integer> stringConverter = new StringConverter<Integer>() {
            @Override
            public String toString(Integer object) {
                if (object == null) {
                    return null;
                }
                return object.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    new StdError("Error", "not a number!", "You haven't typed a number!", Alert.AlertType.ERROR);
                    return 0;
                }
            }
        };
        setCellFactory(column -> {
            TableCell<Period, Integer> cell = new TextFieldTableCell<Period, Integer>(stringConverter);
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        try {
            setOnEditCommit(event -> update(event.getRowValue(), event.getNewValue(), event.getRowValue().getMinute()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * sets propertyValue factory and field controller
     */
    public void setup(Controller controller, PropertyValueFactory<Period, Integer> propertyValueFactory) {
        this.controller = controller;
        setCellValueFactory(propertyValueFactory);

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
