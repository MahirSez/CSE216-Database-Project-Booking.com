import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Types.BOOLEAN;

public class LoginController {
    private BookingClient bookingClient;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXPasswordField passwordField ;

    @FXML
    private void LogInButtonClicked() {
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println(email + " " + password);

        //connect
        DbAdapter dbAdapter = new BookingDbImpl();
        dbAdapter.connect();


        try {
            String sql = "{call safe_to_login(?,MD5(?) )}";
            CallableStatement statement = dbAdapter.conn.prepareCall(sql);
            statement.setString(1 , email);
            statement.setString(2 , password);
            statement.registerOutParameter(1, BOOLEAN);
            statement.execute();

            Boolean safe = statement.getBoolean(1);


            if( !safe ) {

                Notifications notifications = Notifications.create()
                        .title("Login Failed!")
                        .text("Invalid username or password")
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

        Notifications notifications = Notifications.create()
                .title("Confirmation!")
                .text("Successfully Logged in")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_RIGHT);
        notifications.showConfirm();
        System.out.println(password + " " + email);

        //disconnect
        dbAdapter.disconnect();

        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private  void RegisterButtonClicked() {
        try {
            bookingClient.showRegistrationMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }
}
