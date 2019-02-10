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
import java.sql.*;
import java.util.*;

public class ListedHotelsController implements Initializable{
    private BookingClient bookingClient;
    private String cityName;


    @FXML
    private JFXTreeTableView<ListedHotel> treeView;

    ObservableList<ListedHotel> hotelList ;

    @FXML
    JFXTreeTableColumn<ListedHotel, String> hotelNameColumn;

    @FXML
    JFXTreeTableColumn<ListedHotel, String> hotelRatingColumn;


    @FXML
    private void homeLinkClicked() {
        try {
         bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
         e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hotelList = FXCollections.observableArrayList();
        hotelNameColumn = new JFXTreeTableColumn<>("Hotel Name");
        hotelNameColumn.setPrefWidth(350);
        hotelNameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedHotel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedHotel, String> param) {
                return param.getValue().getValue().hotelName;
            }
        });


        hotelRatingColumn = new JFXTreeTableColumn<>("Rating");
        hotelRatingColumn.setPrefWidth(400);
        hotelRatingColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedHotel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedHotel, String> param) {
                return param.getValue().getValue().rating;
            }
        });
    }

    private void populateTable() {

        final TreeItem<ListedHotel> root = new RecursiveTreeItem<ListedHotel>(hotelList, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(hotelNameColumn , hotelRatingColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }

    private void executeSQL() {

        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();

        try {
            String SQL = "select * from hotel_search(?)";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setString(1 , cityName);
            ResultSet result = statement.executeQuery();

            while( result.next()) {
                int hotelID = result.getInt(1);
                String hotelName = result.getString(2);
                Double hotelRating = result.getDouble(3);
                //System.out.println(hotelID + " "  + hotelName + " " + hotelRating);
                hotelList.add(new ListedHotel(hotelName , hotelRating));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbAdapter.disconnect();

        populateTable();


    }



    public void setBookingClient(BookingClient bookingClient, String cityName) {
        this.bookingClient = bookingClient;
        this.cityName = cityName;

        executeSQL();
    }

    private static final class ListedHotel extends RecursiveTreeObject<ListedHotel> {
        final StringProperty hotelName;
        final StringProperty rating;

        public ListedHotel(String hotelName, Double rating) {
            this.hotelName = new SimpleStringProperty(hotelName);
            this.rating = new SimpleStringProperty(rating.toString());
        }
    }
}

