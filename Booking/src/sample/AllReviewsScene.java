package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AllReviewsScene implements Initializable {

    public TableView<Review> allReviews;
    public TableColumn usernameCol;
    public TableColumn descriptionCol;
    public TableColumn ratingCol;
    public Button goBack;
    public Label hotelName;

    public String getAllReviewString() {
        String sql = "select * from review where property_id = ?";
        return sql;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));



        System.out.println("initializng All Reviews scene");

        hotelName.setText(RoomScene.hName);

        ResultSet rs;
        try (PreparedStatement stmt = Main.con.prepareStatement(getAllReviewString())) {
            stmt.setInt(1, Integer.parseInt(ResultScene.selectedProperty));
            rs = stmt.executeQuery();

            while(rs.next()) {
                String usn = rs.getString("user_id");
                String desc = rs.getString("review_description");
                String rat = rs.getString("rating");

                Review review = new Review(usn, desc, rat);
                allReviews.getItems().add(review);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBackClicked(ActionEvent event) throws Exception{
        if (SearchScene.pType==1) {
            Parent root = FXMLLoader.load(getClass().getResource("roomlist.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("carlist.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));
        }
    }

    public class Review {
        private String username;
        private String description;
        private String rating;


        public Review(String username, String description, String rating) {
            this.username = username;
            this.description = description;
            this.rating = rating;
        }

        public String getUsername() {
            return username;
        }

        public String getDescription() {
            return description;
        }

        public String getRating() {
            return rating;
        }
    }
}
