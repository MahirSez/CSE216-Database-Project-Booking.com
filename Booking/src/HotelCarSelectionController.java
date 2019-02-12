import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
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
    private JFXTextField priceFrom;

    @FXML
    private JFXTextField priceTo;

    @FXML
    private JFXTextField numberOfPersons;

    @FXML
    private JFXComboBox selection ;

    @FXML
    private JFXDatePicker checkInDate;

    @FXML
    private  JFXDatePicker checkOutDate;

    ObservableList<String> propertyList = FXCollections
            .observableArrayList("Hotel", "Car Rental");

    String typeOfProperty = "";

    @FXML
    private void hotelCarSelection() {


        typeOfProperty = (String) selection.getValue();
    }
    @FXML
    private void reviewButtonClicked() {
        try {
            bookingClient.showReviewScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void logOutClicked() {
        try {
            bookingClient.showLoginMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void profileButtonClicked() {
        try {
            bookingClient.showProfileScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @FXML
    private  void submitButtonClicked() {
        bookingClient.cityName = cityField.getText();
        bookingClient.priceFrom = priceFrom.getText();
        bookingClient.priceTo = priceTo.getText();
        bookingClient.numberOFPersons = numberOfPersons.getText();
        bookingClient.checkInDate = checkInDate.getValue();
        bookingClient.getCheckOutDate = checkOutDate.getValue();

//
//                System.out.println("Name : " + bookingClient.cityName);
//                System.out.println("Price From : " + bookingClient.priceFrom);
//                System.out.println("Price To : " + bookingClient.priceTo);
//                System.out.println("numberOFPersons  : " + bookingClient.numberOFPersons);
//                System.out.println("checkInDate  : " + bookingClient.checkInDate);
//                System.out.println("getCheckOutDate  : " + bookingClient.getCheckOutDate);



        if( typeOfProperty.equals("Hotel")) {
            try {
                bookingClient.showListedHotels();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if( typeOfProperty.equals("Car Rental")){
            try {
                bookingClient.showCarList();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
