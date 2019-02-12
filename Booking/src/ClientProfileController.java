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
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class ClientProfileController implements Initializable {

    BookingClient bookingClient;

    @FXML
    private JFXTreeTableView<History> treeView;

    @FXML
    private Text userName;

    @FXML
    private Text userMail;

    @FXML
    private Text userPhoneNumber;

    @FXML
    private Text userNationality;

    @FXML
    JFXTreeTableColumn<History, String> hotelNameColumn;

    @FXML
    JFXTreeTableColumn<History, String> checkInDateColumn;

    @FXML
    JFXTreeTableColumn<History, String> checkOutDateColumn;

    @FXML
    JFXTreeTableColumn<History, String> priceColumn;

    private ObservableList<History> historyList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        historyList = FXCollections.observableArrayList();

        hotelNameColumn = new JFXTreeTableColumn<>("Hotel Name");
        hotelNameColumn.setPrefWidth(235);
        hotelNameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<History, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<History, String> param) {
                return param.getValue().getValue().hotelName;
            }
        });

        checkInDateColumn = new JFXTreeTableColumn<>("Check-in Date");
        checkInDateColumn.setPrefWidth(200);
        checkInDateColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<History, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<History, String> param) {
                return param.getValue().getValue().checkInDate;
            }
        });

        checkOutDateColumn = new JFXTreeTableColumn<>("Check-out Date");
        checkOutDateColumn.setPrefWidth(200);
        checkOutDateColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<History, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<History, String> param) {
                return param.getValue().getValue().checkOutDate;
            }
        });

        priceColumn = new JFXTreeTableColumn<>("Price");
        priceColumn.setPrefWidth(120);
        priceColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<History, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<History, String> param) {
                return param.getValue().getValue().cost;
            }
        });

    }

    @FXML
    void backButtonClicked() {
        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homeLinkClicked() {
        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void populateTable() {



        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();

        try {
            String SQL = "select * from get_reservations_of_a_client(?)";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setInt(1, bookingClient.clientID);
            ResultSet result = statement.executeQuery();

            while( result.next() ) {
                Date CheckInDate = result.getDate(1);
                Date CheckOutDate = result.getDate(2);
                String hotelName = result.getString(3);
                int price = result.getInt(4);
                System.out.println(CheckInDate + " " + CheckOutDate + " " + hotelName + " " + price);

                historyList.add(new History(hotelName ,CheckInDate , CheckOutDate ,  price));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();



        final TreeItem<History> root = new RecursiveTreeItem<History>(historyList, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(hotelNameColumn , checkInDateColumn , checkOutDateColumn , priceColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;


        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();


        try{
            String sql = "select client_name , email , mobile_n0 , nationality from client where client_id = ?";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);

            statement.setInt(1 , bookingClient.clientID);
            ResultSet result = statement.executeQuery();

            if( result.next() ) {
                userName.setText(result.getString(1));
                userMail.setText(result.getString(2));
                userPhoneNumber.setText(result.getString(3));
                userNationality.setText(result.getString(4));

            }

        } catch (Exception e ) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();

        populateTable();




    }

    private static final class History extends RecursiveTreeObject<History> {
        final StringProperty hotelName;
        final StringProperty checkInDate;
        final StringProperty checkOutDate;
        final StringProperty cost;

        public History(String hotelName, Date checkInDate, Date checkOutDate, Integer cost) {
            this.hotelName = new SimpleStringProperty(hotelName);
            this.checkInDate = new SimpleStringProperty(checkInDate.toString());
            this.checkOutDate = new SimpleStringProperty(checkOutDate.toString() );
            this.cost = new SimpleStringProperty(cost.toString() );
        }
    }

}
