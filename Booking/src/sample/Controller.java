package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.CallableStatement;
import java.sql.Types;

public class Controller {

    public Button loginButton;
    public TextField username;
    public PasswordField password;

    public void loginButtonClicked(ActionEvent event) throws Exception {
        String usn = username.getText();
        String pass = password.getText();

        CallableStatement checkUser = Main.con.prepareCall("{ ? = call \"checkCredentials\"( ?, ? ) }");
        checkUser.registerOutParameter(1, Types.BOOLEAN);
        checkUser.setString(2, usn);
        checkUser.setString(3, pass);
        checkUser.execute();

        boolean result = checkUser.getBoolean(1);

        if(result) {
            System.out.println(usn + " logged in");

            Infos.username = usn;

            Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));
        } else {
            System.out.println("Incorrect username or password");
        }

    }
}
