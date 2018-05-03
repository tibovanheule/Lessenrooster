package timetable.lecture;


import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import timetable.objects.Lecture;

public class ConflictsListview extends ListView<Lecture> {
    public ConflictsListview() {
        setCellFactory(new Callback<ListView<Lecture>, ListCell<Lecture>>() {
            @Override
            public ListCell<Lecture> call(ListView<Lecture> myObjectListView) {
                ListCell<Lecture> cell = new ListCell<Lecture>() {
                    {
                        //gevonden fix voor de wrap text
                        /*enkel setWraptext(true) werkt niet (geen idee waarom, bug mss) hieronder is een gevonden workaround
                         * in feite de breedte van de cell even groot maken als de Listview door die te koppellen aan elkaar (via bind) */
                        prefWidthProperty().bind(this.widthProperty().subtract(20));
                    }

                    @Override
                    protected void updateItem(Lecture item, boolean b) {
                        super.updateItem(item, b);
                        if (b || item == null) {
                            setText(null);
                            setGraphic(null);
                            this.setWrapText(true);
                        } else {
                            setText(item.getCourse());
                        }
                    }
                };
                return cell;
            }
        });

    }


}
