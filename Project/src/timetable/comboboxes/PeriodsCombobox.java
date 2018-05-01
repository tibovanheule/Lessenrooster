package timetable.comboboxes;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import timetable.objects.Period;

/**
 * Custom combobox. for Periods
 *
 * @author Tibo Vanheule
 */
public class PeriodsCombobox extends ComboBox<Period> {
    public PeriodsCombobox() {
        setCellFactory(new Callback<ListView<Period>, ListCell<Period>>() {
            @Override
            public ListCell<Period> call(ListView<Period> p) {
                return new ListCell<Period>() {
                    @Override
                    protected void updateItem(Period item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            setText(item.getHour() + ":" + item.getMinute());
                        }
                    }
                };
            }
        });

    }

}
