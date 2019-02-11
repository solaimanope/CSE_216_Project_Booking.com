package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class RoomScene implements Initializable {

    public TableView<Room> roomsList;
    public Button goBack;
    public Button bookNow;
    public TableColumn roomTypeCol;
    public TableColumn priceCol;
    public TableColumn sleepsCol;
    public TableColumn acCol;
    public TextArea hotelDesc;
    public Label hotelName;
    public Label hotelRating;
    public Button seeAllReviews;
    public static String hName;

    public String getQueryString() {
        String SQL = "select *\n" +
                "from rooms\n" +
                "where property_id = ? and is_valid_room(room_id, to_date(?, 'YYYY-MM-DD'), to_date(?, 'YYYY-MM-DD'), ?)";

        return SQL;
    }
    public String getInfoQueryString() {
        String SQL = "select * from properties\n" +
                "where property_id  = ?";
        return SQL;
    }
    public String getBookingQueryString() {
        String SQL = "CALL add_booking(?, ?, to_date(?, 'YYYY-MM-DD'), to_date(?, 'YYYY-MM-DD'), ?)";
        return SQL;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializng room scene");

        ResultSet rs;
        try (PreparedStatement stmt = Main.con.prepareStatement(getInfoQueryString())) {
            stmt.setInt(1, Integer.parseInt(ResultScene.selectedProperty));
            rs = stmt.executeQuery();

            rs.next();
            hotelName.setText(rs.getString("property_name"));
            hName = hotelName.getText();
            hotelRating.setText(rs.getString("rating"));
            hotelDesc.setText(rs.getString("description"));

            displayRooms(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        roomTypeCol.setCellValueFactory(new PropertyValueFactory<>("rtype"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("rprice"));
        sleepsCol.setCellValueFactory(new PropertyValueFactory<>("rsleeps"));
        acCol.setCellValueFactory(new PropertyValueFactory<>("rac"));

        try (PreparedStatement stmt = Main.con.prepareStatement(getQueryString())) {
            stmt.setInt(1, Integer.parseInt(ResultScene.selectedProperty));
            stmt.setString(2, SearchScene.inDate.toString());
            stmt.setString(3, SearchScene.outDate.toString());
            stmt.setInt(4, SearchScene.persons);

            rs = stmt.executeQuery();
            displayRooms(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // display actor information
    }

    void displayRooms(ResultSet rs) throws Exception {
        System.out.println("fetching rooms");

        while (rs.next()) {
            String rid = rs.getString("room_id");
            String pid = rs.getString("property_id");
            String rtype = rs.getString("room_type");
            String rprice = rs.getString("price");
            String rsleeps = rs.getString("sleeps");
            String rac = rs.getString("air_conditioned");

            Room room = new Room(rid, pid, rtype, rprice, rsleeps, rac);
            System.out.println("fetched " + room);

            roomsList.getItems().add(room);
        }
    }

    public void goBackClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

    public void bookNowClicked(ActionEvent event) throws Exception {
        Room room = roomsList.getSelectionModel().getSelectedItem();
        if (room==null) System.out.println("No room selected yet!");
        else {
            System.out.println("Selected " + room.rid + " " + room.rtype);
            try (CallableStatement stmnt = Main.con.prepareCall(getBookingQueryString())) {
                stmnt.setInt(1, Integer.parseInt(room.getRid()));
                stmnt.setString(2, Infos.username);
                stmnt.setString(3, SearchScene.inDate.toString());
                stmnt.setString(4, SearchScene.outDate.toString());
                stmnt.setInt(5, SearchScene.persons);
                stmnt.execute();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Parent root = FXMLLoader.load(getClass().getResource("review.fxml"));
            Main.stage.setScene(new Scene(root, 600, 500));

        }
    }

    public void SeeAllReviewsClicked(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("allreviews.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

    public class Room {
        private String rid;
        private String pid;
        private String rtype;
        private String rprice;
        private String rsleeps;
        private String rac;


        public Room(String rid, String pid, String rtype, String rprice, String rsleeps, String rac) {
            this.rid = rid;
            this.pid = pid;
            this.rtype = rtype;
            this.rprice = rprice;
            this.rsleeps = rsleeps;
            this.rac = rac;
        }

        public String getRid() {
            return rid;
        }

        public String getPid() {
            return pid;
        }

        public String getRtype() {
            return rtype;
        }

        public String getRprice() {
            return rprice;
        }

        public String getRsleeps() {
            return rsleeps;
        }

        public String getRac() {
            return rac;
        }

        @Override
        public String toString() {
            return "Room{" +
                    "rid='" + rid + '\'' +
                    ", pid='" + pid + '\'' +
                    ", rtype='" + rtype + '\'' +
                    ", rprice='" + rprice + '\'' +
                    ", rsleeps='" + rsleeps + '\'' +
                    ", rac='" + rac + '\'' +
                    '}';
        }
    }

}
