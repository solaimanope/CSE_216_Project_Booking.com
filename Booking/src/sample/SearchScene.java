package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class SearchScene implements Initializable {
    public Label welcomeText;
    public TextField searchBar;
    public Button searchButton;
    public RadioButton radio1;
    public RadioButton radio2;
    public DatePicker inPicker;
    public DatePicker outPicker;
    public Spinner<Integer> spinner;
    public static String searchString;
    public static LocalDate inDate;
    public static LocalDate outDate;
    public static int persons;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        spinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10)
        );

        welcomeText.setText("Welcome, " + Infos.username);
        inPicker.setValue(LocalDate.now());

        final Callback<DatePicker, DateCell> dayCellFactory1 = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(
                                LocalDate.now()
                                )) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        inPicker.setDayCellFactory(dayCellFactory1);

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);

                                if (item.isBefore(
                                        inPicker.getValue().plusDays(1))
                                        ) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        outPicker.setDayCellFactory(dayCellFactory);
        outPicker.setValue(inPicker.getValue().plusDays(1));

    }

    public void searched(ActionEvent event) throws Exception {
        searchString = searchBar.getText();
        inDate = inPicker.getValue();
        outDate = outPicker.getValue();
        persons = spinner.getValue();

        System.out.println(inDate+" "+outDate+" "+persons);

        Parent root = FXMLLoader.load(getClass().getResource("result.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

}
