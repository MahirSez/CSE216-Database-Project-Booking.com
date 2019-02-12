import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReviewSceneController {

    BookingClient bookingClient;

    @FXML
    private JFXTextField hotelNameField;

    @FXML
    private JFXComboBox ratingSelection;

    @FXML
    private JFXTextField cityNameField;

    @FXML
    private JFXTextArea reviewArea;

    @FXML
    private void initialize() {

        ratingSelection.setItems(propertyList);
    }


    private String hotelRating;

    @FXML
    void ratingSelected() {
        hotelRating  = (String) ratingSelection.getValue();
    }

    @FXML
    void homeLinkClicked() {
        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ObservableList<String> propertyList = FXCollections
            .observableArrayList("5", "4" , "3" , "2" , "1");

    @FXML
    void submitButtonClicked() {

        String hotelName = hotelNameField.getText();
        String cityName = cityNameField.getText();
        String reviewDescription = reviewArea.getText();
        Double rating  = Double.parseDouble(hotelRating);
        int hotelId = -1;


        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();

        try {
            String sql = "\n" +
                    "select hotel_id \n" +
                    "from hotel h \n" +
                    "join property p \n" +
                    "on (p.property_id = h.hotel_id)\n" +
                    "join city c\n" +
                    "on (p.city_id = c.city_id)\n" +
                    "where h.hotel_name = ? and  c.city_name = ?;";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);
            statement.setString(1 , hotelName);
            statement.setString(2 , cityName);
            ResultSet resultSet = statement.executeQuery();

            if( resultSet.next()) {
                hotelId = resultSet.getInt(1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        dbAdapter.disconnect();

        System.out.println(hotelId + " " + rating );
        if( hotelId == -1) {
            Notifications notifications = Notifications.create()
                    .title("Invalid Operation")
                    .text("No Hotel Found")
                    .graphic(null)
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_RIGHT);
            notifications.showError();
            dbAdapter.disconnect();
            return;
        }

        else {


            dbAdapter = new DbAdapter();
            dbAdapter.connect();

            try {

                String sql = "insert into hotel_review(hotel_id , client_id , review_date ,  rating , description) values(? , ? , current_date , ? , ?) ";
                PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);

                statement.setInt(1 , hotelId);
                statement.setInt(2 , bookingClient.clientID);
                statement.setDouble(3 , rating);
                statement.setString(4 , reviewDescription);
                statement.executeUpdate();

            } catch (Exception e ) {
                e.printStackTrace();
            }
            dbAdapter.disconnect();
        }

        Notifications notifications = Notifications.create()
                .title("Success!")
                .text("Successfully updated Rating")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
        notifications.showConfirm();

        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }
}
