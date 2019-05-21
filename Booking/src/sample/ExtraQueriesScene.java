package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ExtraQueriesScene implements Initializable {

    public TableView<TopUser> topUsers;
    public TableColumn usernameCol;
    public TableColumn bookingCol;

    public TableView<TopCity> topCities;
    public TableColumn citynameCol;
    public TableColumn countCol;

    public Button goBack;

    public String getTopUsersQuery() {
        String sql = "select user_id us, count(*) cnt from room_booking\n" +
                "group by user_id\n" +
                "order by count(*) desc\n" +
                "limit 5";

        return sql;
    }

    public String getTopCitiesQuery() {
        String sql = "select city_name, user_count\n" +
                "from cities\n" +
                "where user_count > 0\n" +
                "order by user_count desc limit 5";

        return sql;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        bookingCol.setCellValueFactory(new PropertyValueFactory<>("booking"));

        try (PreparedStatement stmt = Main.con.prepareStatement(getTopUsersQuery())) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String uid = rs.getString("us");
                String cont = rs.getString("cnt");

                TopUser topUser = new TopUser(uid, cont);
                //System.out.println(topUser.getnBooking());

                System.out.println(topUser.getUsername()+" "+topUser.getBooking());
                topUsers.getItems().add(topUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        citynameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        try (PreparedStatement stmt = Main.con.prepareStatement(getTopCitiesQuery())) {
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String name = rs.getString("city_name");
                String cnt = rs.getString("user_count");

                TopCity topCity = new TopCity(name, cnt);
                //System.out.println(topUser.getnBooking());

                topCities.getItems().add(topCity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goBackClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

    public class TopUser {
        private String username;
        private String booking;

        public TopUser(String username, String booking) {
            this.username = username;
            this.booking = booking;
        }

        public String getUsername() {
            return username;
        }

        public String getBooking() {
            return booking;
        }
    }

    public class TopCity {
        private String name;
        private String count;

        public TopCity(String name, String count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public String getCount() {
            return count;
        }
    }
}
