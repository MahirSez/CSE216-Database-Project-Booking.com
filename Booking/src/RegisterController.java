import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Types.BOOLEAN;
import static java.sql.Types.INTEGER;

public class RegisterController {
    private BookingClient bookingClient;

    @FXML
    private JFXTextField emailField;

    @FXML
    private JFXPasswordField passwordField ;

    @FXML
    private void RegisterButtonClicked() {
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println(email + " " + password);

        //connect
        DbAdapter dbAdapter = new BookingDbImpl();
        dbAdapter.connect();


        try {
            String sql = "{call email_safe_to_register(?)}";
            CallableStatement statement = dbAdapter.conn.prepareCall(sql);
            statement.setString(1 , email);
            statement.registerOutParameter(1, BOOLEAN);
            statement.execute();

            Boolean safe = statement.getBoolean(1);

            System.out.println(safe);

            if( !safe ) {

                System.out.println();
                dbAdapter.disconnect();
                System.out.println("aage ase");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            String sql = "insert into client(email, password) values(? , ?)";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);
            statement.setString(1 , email);
            statement.setString(2 , password);
            statement.executeUpdate();

            System.out.println("done");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        //disconnect
        dbAdapter.disconnect();
    }

    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
    }
}
