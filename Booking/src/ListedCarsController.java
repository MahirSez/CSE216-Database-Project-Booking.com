import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.util.Callback;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ListedCarsController implements Initializable{



    private BookingClient bookingClient;
    private ObservableList<ListedCars> carList ;
    @FXML
    private JFXTreeTableView<ListedCars> treeView;

    @FXML
    private JFXTreeTableColumn<ListedCars, String> carNameColumn;

    @FXML
    private JFXTreeTableColumn<ListedCars,String> carRatingColumn;

    @FXML
    private JFXTreeTableColumn<ListedCars,String> carPriceColumn;

    @FXML
    void SelectButtonClicked() {
        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
        populate();
    }


    @FXML
    private void homeLinkClicked() {
        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populate() {

        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();

        try {
            String SQL = "select * from car_rental_company_search(?)";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setString(1 , bookingClient.cityName);
            ResultSet result = statement.executeQuery();
            int id =0;
            while( result.next()) {
                int companyID = result.getInt(1);
                String companyName = result.getString(2);
                Double companyRating = result.getDouble(3);
                int price = result.getInt(4);

                //System.out.println(id + " " + hotelID + " " + hotelName);

                carList.add(new ListedCars(companyName , companyRating , price));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        carList = FXCollections.observableArrayList();

        carNameColumn = new JFXTreeTableColumn<>("Car Rental Company Name");
        carNameColumn.setPrefWidth(350);
        carNameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedCars, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedCars, String> param) {
                return param.getValue().getValue().hotelName;
            }
        });
        carNameColumn.setSortable(false);



        carRatingColumn = new JFXTreeTableColumn<>("Rating");
        carRatingColumn.setPrefWidth(400);
        carRatingColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedCars, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedCars, String> param) {
                return param.getValue().getValue().rating;
            }
        });
        carRatingColumn.setSortable(false);


        carPriceColumn = new JFXTreeTableColumn<>("Price");
        carPriceColumn.setPrefWidth(400);
        carPriceColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedCars, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedCars, String> param) {
                return param.getValue().getValue().price;
            }
        });
        carPriceColumn.setSortable(false);


        final TreeItem<ListedCars> root = new RecursiveTreeItem<ListedCars>(carList, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(carNameColumn , carRatingColumn , carPriceColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

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


    private static final class ListedCars extends RecursiveTreeObject<ListedCars> {
        final StringProperty hotelName;
        final StringProperty rating;
        final StringProperty price;



        public ListedCars(String hotelName, Double rating, Integer price) {

            this.hotelName = new SimpleStringProperty(hotelName);
            this.rating = new SimpleStringProperty(rating.toString());
            this.price = new SimpleStringProperty(price.toString());
        }
    }



}
