package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.CallableStatement;
import java.util.ResourceBundle;

public class SignUpScene implements Initializable {
    public Button goBackButton;
    public Button signUpButton;

    public TextField userName;
    public TextField password;
    public TextField name;
    public TextField email;
    public TextField mobile;
    public TextField cityName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializing sign up scene");

    }

    public String getUserQueryString() {
        String sql = "call add_new_user(?, ?, ?, ?, ?, ?)";

        return sql;
    }

    public void goBackClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }
    public void signUpClicked(ActionEvent event) throws Exception {

        try (CallableStatement stmnt = Main.con.prepareCall(getUserQueryString())) {

            stmnt.setString(1, userName.getText());
            stmnt.setString(2, password.getText());
            stmnt.setString(3, name.getText());
            stmnt.setString(4, mobile.getText());
            stmnt.setString(5, email.getText());
            stmnt.setString(6, cityName.getText());

            stmnt.execute();

            Infos.username = userName.getText();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Infos.username = userName.getText();
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }
}
