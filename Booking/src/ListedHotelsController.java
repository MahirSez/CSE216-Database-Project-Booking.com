import com.jfoenix.controls.*;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import sun.reflect.generics.tree.Tree;

import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ListedHotelsController implements Initializable{
    private BookingClient bookingClient;


    @FXML
    private JFXTreeTableView<ListedHotel> treeView;

    ObservableList<ListedHotel> hotelList ;

    @FXML
    JFXTreeTableColumn<ListedHotel, String> hotelNameColumn;

    @FXML
    JFXTreeTableColumn<ListedHotel, String> hotelRatingColumn;
    Map<Integer, Integer> map = new HashMap<Integer, Integer>();



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
    private void homeLinkClicked() {
        try {
         bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
         e.printStackTrace();
        }
    }
    @FXML
    void SelectButtonClicked() {
        TreeItem<ListedHotel> selectedItem ;
        selectedItem = treeView.getSelectionModel().getSelectedItem();
        if( selectedItem == null  ) {
            return;
        }

        int id = selectedItem.getParent().getChildren().indexOf(selectedItem);
        bookingClient.hotelID = map.get(id);


        try {
            bookingClient.showHotelDescription();
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
        hotelNameColumn.setSortable(false);



        hotelRatingColumn = new JFXTreeTableColumn<>("Rating");
        hotelRatingColumn.setPrefWidth(400);
        hotelRatingColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedHotel, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedHotel, String> param) {
                return param.getValue().getValue().rating;
            }
        });
        hotelRatingColumn.setSortable(false);

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
            String SQL = "select * from hotel_search(?, ? , ? , ? , ? , ?);";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setString(1 , bookingClient.cityName);
            statement.setInt(2 , Integer.parseInt(bookingClient.priceFrom) );
            statement.setInt(3 , Integer.parseInt(bookingClient.priceTo) );
            statement.setDate(4 , Date.valueOf(bookingClient.checkInDate) );
            statement.setDate(5 , Date.valueOf(bookingClient.getCheckOutDate));
            statement.setInt(6 , Integer.parseInt(bookingClient.numberOFPersons));
            ResultSet result = statement.executeQuery();
            int id =0;
            while( result.next()) {
                int hotelID = result.getInt(1);
                String hotelName = result.getString(2);
                Double hotelRating = result.getDouble(3);
                System.out.println(hotelID + " "  + hotelName + " " + hotelRating);
                //System.out.println(id + " " + hotelID + " " + hotelName);
                map.put(id++ , hotelID);
                hotelList.add(new ListedHotel(hotelName , hotelRating));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dbAdapter.disconnect();

        populateTable();


    }




    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;

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

