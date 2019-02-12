import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.LocalDate;
import java.util.Date;

//Client
public class BookingClient extends Application {

    Stage stage;
    String cityName , priceFrom , priceTo  , numberOFPersons ;
    int clientID , hotelID;

    LocalDate checkInDate , getCheckOutDate;

    /***
     * Delete this function for fucks sake
     */
    void setValues() {

        cityName = "Delhi";
        priceFrom = "0";
        priceTo  = "1234567";
        numberOFPersons = "5";
        clientID = 11;
        String str = "";
        checkInDate =LocalDate.of(2017 , 2 , 2);
        getCheckOutDate = LocalDate.of(2017 , 2 , 6);
        hotelID = 1;

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

//        setValues();

        stage = primaryStage;

        //stage.initStyle(StageStyle.TRANSPARENT);

        stage.setResizable(false);

//        showHotelDescription(1);
//        showListedRooms(1);
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

    public void showHotelCarSelectionMenu() throws Exception {
        FXMLLoader loader  = new FXMLLoader();

        loader.setLocation(getClass().getResource("HotelCarSelection.fxml") );
        Parent root = loader.load();

        HotelCarSelectionController controller = loader.getController();
        controller.setBookingClient(this);

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }
    public void showListedHotels() throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("ListedHotels.fxml") );
        Parent root = loader.load();

        ListedHotelsController controller = loader.getController();
        controller.setBookingClient(this );

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }
    public void showHotelDescription() throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("hotelDescription.fxml") );
        Parent root = loader.load();

        HotelDescriptionController controller = loader.getController();
        controller.setBookingClient(this);

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }
    public void showListedRooms() throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("ListedRooms.fxml") );
        Parent root = loader.load();

        ListedRoomsController controller = loader.getController();
        controller.setBookingClient(this );

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }

    public void showReviewScene() throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("reviewScene.fxml") );
        Parent root = loader.load();

        ReviewSceneController controller = loader.getController();
        controller.setBookingClient(this );

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }

    public void showProfileScene() throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("ClientProfile.fxml") );
        Parent root = loader.load();

        ClientProfileController controller = loader.getController();
        controller.setBookingClient(this );

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }
    public void showCarList() throws Exception {
        FXMLLoader loader  = new FXMLLoader();
        loader.setLocation(getClass().getResource("ListedCars.fxml") );
        Parent root = loader.load();

        ListedCarsController controller = loader.getController();
        controller.setBookingClient(this );

        stage.setScene(new Scene(root) );

        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}
