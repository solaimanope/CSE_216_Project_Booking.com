package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.postgresql.core.SqlCommandType;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.ResourceBundle;

public class ResultScene implements Initializable {
    public static String selectedProperty;

    public TableView<Property> hotelsList;
    public Button goBack;
    public Button proceed;
    public TableColumn propertyCol;
    public TableColumn priceCol;
    public TableColumn ratingCol;

    public String getQueryString() {
        String SQL = "select P.property_id pid, P.property_name pname, P.rating prating\n" +
                "from properties P join cities C on (P.city_id=C.city_id)\n" +
                "where P.property_type = ? and upper(C.city_name) like ?";

        return SQL;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializng result scene");

        propertyCol.setCellValueFactory(new PropertyValueFactory<>("pname"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("pprice"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("prating"));

        ResultSet rs;
        try (PreparedStatement stmt = Main.con.prepareStatement(getQueryString())) {
            stmt.setInt(1, 1);
            stmt.setString(2, "%"+SearchScene.searchString.toUpperCase()+"%");
            rs = stmt.executeQuery();
            displayProperties(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // display actor information
    }

    void displayProperties(ResultSet rs) throws Exception {
        System.out.println("rs");

        while (rs.next()) {
            String pid = rs.getString("pid");
            String pname = rs.getString("pname");
            String prating = rs.getString("prating");
            System.out.println(pid + " " + pname + " " + prating);
            hotelsList.getItems().add(new Property(pid, pname, "100", prating));
        }
    }

    public void goBackClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

    public void proceedClicked(ActionEvent event) throws Exception {
        Property property = hotelsList.getSelectionModel().getSelectedItem();
        if (property==null) System.out.println("No property selected yet!");
        else {
            System.out.println("Selected " + property.pid + " " + property.pname);
            selectedProperty = property.pid;
            Parent root = FXMLLoader.load(getClass().getResource("roomlist.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));
        }
    }

    public class Property {
        private String pid;
        private String pname;
        private String pprice;
        private String prating;

        public Property(String pid, String pname, String pprice, String prating) {
            this.pid = pid;
            this.pname = pname;
            this.pprice = pprice;
            this.prating = prating;
        }

        public String getPid() {
            return pid;
        }

        public String getPname() {
            return pname;
        }

        public String getPprice() {
            return pprice;
        }

        public String getPrating() {
            return prating;
        }
    }

}
