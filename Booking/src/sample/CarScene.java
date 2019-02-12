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

public class CarScene implements Initializable {

    public TableView<Car> carsList;

    public Button goBack;
    public Button bookNow;

    public TableColumn carNameCol;
    public TableColumn seatsCol;
    public TableColumn priceCol;
    public TableColumn acCol;
    public TableColumn gearCol;
    public TableColumn bagsCol;
    public TableColumn doorsCol;

    public TextArea companyDesc;
    public Label companyName;
    public Label companyRating;
    public Button seeAllReviews;

    public String getQueryString() {
        String SQL = "select *\n" +
                "from cars\n" +
                "where property_id = ? and is_valid_car(car_id, to_date(?, 'YYYY-MM-DD'), to_date(?, 'YYYY-MM-DD'), ?)";

        return SQL;
    }
    public String getInfoQueryString() {
        String SQL = "select * from properties\n" +
                "where property_id  = ?";
        return SQL;
    }
    public String getBookingQueryString() {
        String SQL = "CALL add_car_booking(?, ?, to_date(?, 'YYYY-MM-DD'), to_date(?, 'YYYY-MM-DD'), ?)";
        return SQL;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("initializng car scene");

        ResultSet rs;
        try (PreparedStatement stmt = Main.con.prepareStatement(getInfoQueryString())) {
            stmt.setInt(1, Integer.parseInt(ResultScene.selectedProperty));
            rs = stmt.executeQuery();

            rs.next();
            companyName.setText(rs.getString("property_name"));
            companyRating.setText(rs.getString("rating"));
            companyDesc.setText(rs.getString("description"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        carNameCol.setCellValueFactory(new PropertyValueFactory<>("cname"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cprice"));
        seatsCol.setCellValueFactory(new PropertyValueFactory<>("cseats"));
        acCol.setCellValueFactory(new PropertyValueFactory<>("cac"));
        doorsCol.setCellValueFactory(new PropertyValueFactory<>("cdoors"));
        bagsCol.setCellValueFactory(new PropertyValueFactory<>("cbags"));
        gearCol.setCellValueFactory(new PropertyValueFactory<>("cgear"));

        try (PreparedStatement stmt = Main.con.prepareStatement(getQueryString())) {
            stmt.setInt(1, Integer.parseInt(ResultScene.selectedProperty));
            stmt.setString(2, SearchScene.inDate.toString());
            stmt.setString(3, SearchScene.outDate.toString());
            stmt.setInt(4, SearchScene.persons);

            rs = stmt.executeQuery();
            displayCars(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // display actor information
    }

    void displayCars(ResultSet rs) throws Exception {
        System.out.println("fetching rooms");

        while (rs.next()) {
            String cid = rs.getString("car_id");
            String pid = rs.getString("property_id");
            String cname = rs.getString("car_name");
            String cprice = rs.getString("price");
            String cseats = rs.getString("seats");
            String cac = rs.getString("air_conditioned");
            String cgear = rs.getString("gearbox");
            String cbags = rs.getString("large_bags");
            String cdoors = rs.getString("doors");

            Car car = new Car(cid, pid, cname, cprice, cseats, cac, cgear, cbags, cdoors);
            System.out.println("fetched " + car);

            carsList.getItems().add(car);
        }
    }

    public void goBackClicked(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("result.fxml"));
        Main.stage.setScene(new Scene(root, 600, 500));
    }

    public void bookNowClicked(ActionEvent event) throws Exception {
        Car car = carsList.getSelectionModel().getSelectedItem();
        if (car==null) System.out.println("No room selected yet!");
        else {
            System.out.println("Selected " + car.cid + " " + car.cname);

            try (CallableStatement stmnt = Main.con.prepareCall(getBookingQueryString())) {
                stmnt.setInt(1, Integer.parseInt(car.getCid()));
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

    public class Car {
        private String cid;
        private String pid;
        private String cname;
        private String cprice;
        private String cseats;
        private String cac;
        private String cgear;
        private String cbags;
        private String cdoors;

        public Car(String cid, String pid, String cname, String cprice, String cseats, String cac, String cgear, String cbags, String cdoors) {
            this.cid = cid;
            this.pid = pid;
            this.cname = cname;
            this.cprice = cprice;
            this.cseats = cseats;
            this.cac = cac;
            this.cgear = cgear;
            this.cbags = cbags;
            this.cdoors = cdoors;
        }

        public String getCid() {
            return cid;
        }

        public String getPid() {
            return pid;
        }

        public String getCname() {
            return cname;
        }

        public String getCprice() {
            return cprice;
        }

        public String getCseats() {
            return cseats;
        }

        public String getCac() {
            return cac;
        }

        public String getCgear() {
            return cgear;
        }

        public String getCbags() {
            return cbags;
        }

        public String getCdoors() {
            return cdoors;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "cid='" + cid + '\'' +
                    ", pid='" + pid + '\'' +
                    ", cname='" + cname + '\'' +
                    ", cprice='" + cprice + '\'' +
                    ", cseats='" + cseats + '\'' +
                    ", cac='" + cac + '\'' +
                    ", cgear='" + cgear + '\'' +
                    ", cbags='" + cbags + '\'' +
                    ", cdoors='" + cdoors + '\'' +
                    '}';
        }
    }

}
