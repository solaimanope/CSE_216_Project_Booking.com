package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchScene implements Initializable {
    public Label welcomeText;
    public TextField searchBar;
    public Button searchButton;
    public RadioButton radio1;
    public RadioButton radio2;
    public static String searchString;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        welcomeText.setText("Welcome, " + Infos.username);
    }

    public void searched(ActionEvent event) throws Exception {
        searchString = searchBar.getText();

        Parent root = FXMLLoader.load(getClass().getResource("result.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

}
