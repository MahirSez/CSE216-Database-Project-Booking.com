import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Types.BOOLEAN;

public class HotelCarSelectionController {
    private BookingClient bookingClient;

    @FXML
    private JFXTextField cityField;

    @FXML
    private JFXComboBox selection ;

    ObservableList<String> propertyList = FXCollections
            .observableArrayList("Hotel", "Car Rental");

    String typeOfProperty = "";

    @FXML
    private void hotelCarSelection() {


        typeOfProperty = (String) selection.getValue();
    }

    @FXML
    private  void submitButtonClicked() {
        try {


            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }
    @FXML
    private void initialize() {

        selection.setItems(propertyList);
    }


}
