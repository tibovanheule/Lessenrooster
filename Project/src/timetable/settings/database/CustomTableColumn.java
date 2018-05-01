package timetable.settings.database;

import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import timetable.Controller;
import timetable.db.DataAccessContext;
import timetable.db.DataAccessException;
import timetable.objects.Period;

/**
 * Custom table column, cellfactory
 * @author Tibo Vanheule*/
public class CustomTableColumn extends TableColumn<Period, Integer> {
    private Controller controller;

    /**
     * constructor, sets cellfactory and on edit*/
    public CustomTableColumn() {
        setCellFactory(column -> {
            javafx.scene.control.TableCell<Period, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
            cell.setAlignment(Pos.CENTER);
            return cell;
        });
        setOnEditCommit(event -> update(event.getRowValue(), event.getNewValue(), event.getRowValue().getMinute()));
    }

    /**
     * sets propertyValue factory and field controller*/
    public void setup(Controller controller, PropertyValueFactory propertyValueFactory) {
        this.controller = controller;
        setCellValueFactory(propertyValueFactory);

    }

    /**
     * updates a period*/
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
