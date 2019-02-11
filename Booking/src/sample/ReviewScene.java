package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;


import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ResourceBundle;

import static java.sql.Types.NUMERIC;

public class ReviewScene implements Initializable {

    public Label hotelName;
    public TextArea description;
    public Button submit;
    public Slider slider;

    public String getReviewQueryString() {
        String sql = "call add_new_review(?, ?, ?, cast(? as numeric))";

        return sql;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializng Review scene");

        hotelName.setText(RoomScene.hName);


        // display actor information
    }

    public void submitButtonClicked(ActionEvent event) {

        try (CallableStatement stmnt = Main.con.prepareCall(getReviewQueryString())) {

            stmnt.setString(1, Infos.username);
            stmnt.setInt(2, Integer.parseInt(ResultScene.selectedProperty));
            stmnt.setString(3, description.getText());
            stmnt.setDouble(4, slider.getValue());

            stmnt.execute();

            Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //System.out.println(slider.getValue());
    }
}
