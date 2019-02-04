import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//Client
public class BookingClient extends Application {

    Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception{



        stage = primaryStage;
        //stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        stage.setResizable(false);

        showLoginMenu();
    }

    public void showLoginMenu()  throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("Login.fxml") );
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setBookingClient(this);

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }

    public void showRegistrationMenu() throws Exception {

        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("Registration.fxml") );
        Parent root = loader.load();

        RegisterController controller = loader.getController();
        controller.setBookingClient(this);

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {

        launch(args);


    }
}
