import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Types.BOOLEAN;

public class RegisterController {
    private BookingClient bookingClient;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField mobileNo;
    @FXML
    private JFXTextField nationality;

    @FXML
    private void homeLinkClicked() {
        try {
            bookingClient.showLoginMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void RegisterButtonClicked() {
        String mailID = emailField.getText();
        String pass = passwordField.getText();
        String fullName = name.getText();
        String mobileNumber = mobileNo.getText();
        String nation = nationality.getText();

        if( mailID.length() ==0 || pass.length() ==0 || fullName.length() ==0 ) {

            Notifications notifications = Notifications.create()
                    .title("Registration Failed!!")
                    .graphic(null)
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_RIGHT);
            notifications.showError();
            return;
        }

        //connect
        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();


        try {
            String sql = "{call email_safe_to_register(?)}";
            CallableStatement statement = dbAdapter.conn.prepareCall(sql);
            statement.setString(1 , mailID);
            statement.registerOutParameter(1, BOOLEAN);
            statement.execute();

            Boolean safe = statement.getBoolean(1);

            if( !safe ) {

                Notifications notifications = Notifications.create()
                        .title("Registration Failed!!")
                        .text("this mail has already been registered")
                        .graphic(null)
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.TOP_RIGHT);
                notifications.showError();
                dbAdapter.disconnect();

                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        
        try {
            String sql = "insert into client(CLIENT_NAME , email, password , MOBILE_N0 , NATIONALITY) values(? , ?, MD5(?) , ? , ?)";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);
            statement.setString(1 , fullName);
            statement.setString(2 , mailID);
            statement.setString(3 , pass);
            statement.setString(4 , mobileNumber);
            statement.setString(5 , nation);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Notifications notifications = Notifications.create()
                .title("Confirmation!")
                .text("Successfully Registered")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
        notifications.showConfirm();

        try {
            String SQL = "select client_id from client where email = ? ";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setString(1 , mailID);
            ResultSet result = statement.executeQuery();
            int id =0;
            while( result.next()) {
                id = result.getInt(1);

                bookingClient.clientID = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //disconnect
        dbAdapter.disconnect();


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
