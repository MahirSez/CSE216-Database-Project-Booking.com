import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.cells.editors.base.JFXTreeTableCell;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTreeCell;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.Notifications;

import javax.xml.ws.soap.MTOM;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ListedRoomsController {

    private BookingClient bookingClient;


    ObservableList<ListedRooms> roomList;
    ObservableList<ListedRooms> selectedRoomList;
    @FXML
    private Label hotelNameLabel;

    @FXML
    private JFXTreeTableView<ListedRooms> treeView1;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> roomTypeColumn;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> capacityColumn;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> facilitiesColumn;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> priceColumn;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> quantityColumn;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> quantitySelectionColumn;

    @FXML
    private JFXTreeTableView<ListedRooms> treeView2;

    @FXML
    private JFXTreeTableColumn<ListedRooms, String> selectedRoomColumn;

    @FXML
    private TreeTableColumn<ListedRooms, String> selectedQuantityColumn;

    ObservableList<String> list = FXCollections.observableArrayList();
    Map<String, Integer> map = new HashMap<>();


    @FXML
    void selectButtonClicked() {
        TreeItem<ListedRooms> selectedItem;
        selectedItem = treeView1.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            return;
        }

        int id = selectedItem.getParent().getChildren().indexOf(selectedItem);

        String roomType = selectedItem.getValue().roomType.getValue();
        int quantitySelected = Integer.parseInt(selectedItem.getValue().selectedAmount.getValue());
        int available = Integer.parseInt(selectedItem.getValue().quantityAvailable.getValue());
        int price = Integer.parseInt(selectedItem.getValue().price.getValue());
        int capacity = Integer.parseInt(selectedItem.getValue().capacity.getValue());
        String facilities = selectedItem.getValue().facilities.getValue();

        Integer alreadyExists = map.get(roomType);
        if (alreadyExists == null) alreadyExists = 0;
        if (quantitySelected + alreadyExists > available) {
            Notifications notifications = Notifications.create()
                    .title("Error")
                    .text("Selected amount of room exceed capacity")
                    .graphic(null)
                    .hideAfter(Duration.seconds(3))
                    .position(Pos.TOP_RIGHT);
            notifications.showError();
        } else if (quantitySelected > 0) {

            System.out.println(roomType + " " + quantitySelected);

            map.put(roomType, alreadyExists + quantitySelected);

            selectedRoomList.add(new ListedRooms(roomType, price, capacity, facilities, available, quantitySelected));
        }


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
    void homeLinkClicked() {
        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tableListShow() {


    }

    public void showTreeTable() {


        //treeView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        roomList = FXCollections.observableArrayList();
        selectedRoomList = FXCollections.observableArrayList();

        quantityColumn.setEditable(true);

        roomTypeColumn = new JFXTreeTableColumn<>("Room type");
        roomTypeColumn.setPrefWidth(191);
        roomTypeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().roomType;
            }
        });
        roomTypeColumn.setSortable(false);


        capacityColumn = new JFXTreeTableColumn<>("Capacity");
        capacityColumn.setPrefWidth(143);
        capacityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().capacity;
            }
        });
        capacityColumn.setSortable(false);

        facilitiesColumn = new JFXTreeTableColumn<>("Facilities");
        facilitiesColumn.setPrefWidth(270);
        facilitiesColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().facilities;
            }
        });
        facilitiesColumn.setSortable(false);


        priceColumn = new JFXTreeTableColumn<>("Price");
        priceColumn.setPrefWidth(162);
        priceColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().price;
            }
        });
        priceColumn.setSortable(false);

        quantityColumn = new JFXTreeTableColumn<>("Available Amount");
        quantityColumn.setPrefWidth(190);
        quantityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().quantityAvailable;
            }
        });
        quantitySelectionColumn.setSortable(false);

        quantitySelectionColumn = new JFXTreeTableColumn<>("Select Quantity");
        quantitySelectionColumn.setPrefWidth(197);
        quantitySelectionColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().selectedAmount;
            }
        });

        quantitySelectionColumn.setCellFactory(new Callback<TreeTableColumn<ListedRooms, String>, TreeTableCell<ListedRooms, String>>() {
            @Override
            public TreeTableCell<ListedRooms, String> call(TreeTableColumn<ListedRooms, String> param) {
                return new TextFieldTreeTableCell<>();
            }
        });

        quantitySelectionColumn.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(list));


        final TreeItem<ListedRooms> root1 = new RecursiveTreeItem<ListedRooms>(roomList, RecursiveTreeObject::getChildren);
        treeView1.getColumns().setAll(roomTypeColumn, capacityColumn, facilitiesColumn, priceColumn, quantityColumn, quantitySelectionColumn);
        treeView1.setRoot(root1);
        treeView1.setShowRoot(false);

        selectedRoomColumn = new JFXTreeTableColumn<>("Room type");
        selectedRoomColumn.setPrefWidth(382);
        selectedRoomColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().roomType;
            }
        });

        selectedQuantityColumn = new JFXTreeTableColumn<>("Selected Quantity");
        selectedQuantityColumn.setPrefWidth(375);
        selectedQuantityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().selectedAmount;
            }
        });

        final TreeItem<ListedRooms> root2 = new RecursiveTreeItem<ListedRooms>(selectedRoomList, RecursiveTreeObject::getChildren);
        treeView2.getColumns().setAll(selectedRoomColumn, selectedQuantityColumn);
        treeView2.setRoot(root2);
        treeView2.setShowRoot(false);


    }

    public void showHotelName() {


        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();
        try {
            String sql = "select hotel_name from hotel where hotel_id = ?;";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);
            statement.setInt(1, bookingClient.hotelID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString(1);
                hotelNameLabel.setAlignment(Pos.CENTER);

            }

            statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();
    }

    private int generateReservationID() {
        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();
        int id = -1;

        try {

            String SQL = "select * from reservation_insert(? , ? , ? , ? );";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setInt(1, bookingClient.hotelID);
            statement.setDate(2, Date.valueOf(bookingClient.checkInDate));
            statement.setDate(3, Date.valueOf(bookingClient.getCheckOutDate));
            statement.setInt(4, bookingClient.clientID);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                id = result.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();

        return id;
    }

    @FXML
    private void confirmClicked() {

        int reservationID = generateReservationID();

        if( reservationID ==-1) {
            System.out.println("Error ! Can't Get reservation ID");
            return;
        }



        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();

        for(String room_type : map.keySet() ) {
            System.out.println(room_type + " " + map.get(room_type));

            try {
                String SQL = "select * from room_reserve_room_id_insert(?,?,?,?,?,?)";

                PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
                statement.setInt(1 , reservationID );
                statement.setString(2 ,room_type );
                statement.setInt(3 ,map.get(room_type) );
                statement.setDate(4 , Date.valueOf(bookingClient.checkInDate) );
                statement.setDate(5, Date.valueOf(bookingClient.getCheckOutDate) );
                statement.setInt(6 , bookingClient.hotelID);
                ResultSet result = statement.executeQuery();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dbAdapter.disconnect();


        Notifications notifications = Notifications.create()
                .title("Confirmation!")
                .text("Reservation Complete")
                .graphic(null)
                .hideAfter(Duration.seconds(3))
                .position(Pos.TOP_CENTER);
        notifications.showConfirm();

        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void executeQuery() {



        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();


        try {
            String SQL = "select * from GET_ALL_Candidate_ROOMS_OF_HOTEL(?,?, ?,?,?, ? )";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setInt(1 , bookingClient.hotelID);
            statement.setInt(2 , Integer.parseInt(bookingClient.priceFrom) );
            statement.setInt(3 , Integer.parseInt(bookingClient.priceTo) );
            statement.setDate(4 , Date.valueOf(bookingClient.checkInDate) ) ;
            statement.setDate(5 , Date.valueOf(bookingClient.getCheckOutDate) ) ;
            statement.setInt(6 , Integer.parseInt(bookingClient.numberOFPersons));

            ResultSet result = statement.executeQuery();

            while( result.next()) {

                String room_type = result.getString(1);
                int price = result.getInt(2);
                int capacity = result.getInt(3);
                String facilities = result.getString(4);
                int quantityAvailable = result.getInt(5);


                roomList.add(new ListedRooms( room_type , price , capacity , facilities , quantityAvailable ,0 ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();
    }

    public void setBookingClient(BookingClient bookingClient) {
        this.bookingClient = bookingClient;


        System.out.println(bookingClient.clientID + " " + bookingClient.hotelID + " " + bookingClient.checkInDate + " "+ bookingClient.getCheckOutDate +
        " " + bookingClient.priceFrom + " " + bookingClient.priceTo);

        for(Integer i = 0 ;i <= 5 ; i++ ) {
            list.add(i.toString());
        }

        showHotelName();
        showTreeTable();
        executeQuery();
    }

    private static final class ListedRooms extends RecursiveTreeObject<ListedRooms> {
        final StringProperty roomType;
        final StringProperty price;
        final StringProperty capacity;
        final StringProperty facilities;

        final StringProperty selectedAmount;
        final StringProperty quantityAvailable ;


        public ListedRooms(String  roomType, Integer price, Integer capacity, String facilities, Integer quantityAvailable , Integer selectedQuantity) {

            this.roomType = new SimpleStringProperty(roomType);
            this.capacity = new SimpleStringProperty(capacity.toString());
            this.facilities = new SimpleStringProperty(facilities);
            this.price = new SimpleStringProperty(price.toString());
            //this.quantity = quantity;
            this.quantityAvailable = new SimpleStringProperty(quantityAvailable.toString());
            this.selectedAmount = new SimpleStringProperty(selectedQuantity.toString());
        }
    }
}
