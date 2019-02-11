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
import javafx.util.StringConverter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ListedRoomsController {

    BookingClient bookingClient;
    int hotelID;


    ObservableList<ListedRooms> roomList;
    @FXML
    private Label hotelNameLabel;

    @FXML
    private JFXTreeTableView<ListedRooms> treeView;

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
    void bookRoomClicked() {

    }

    @FXML
    void homeLinkClicked() {

    }

    @FXML
    void tableListShow() {


    }

    public void showTreeTable() {


        roomList = FXCollections.observableArrayList();

        quantityColumn.setEditable(true);
        roomTypeColumn = new JFXTreeTableColumn<>("Room type");
        roomTypeColumn.setPrefWidth(200);
        roomTypeColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().roomType;
            }
        });



        capacityColumn = new JFXTreeTableColumn<>("Capacity");
        capacityColumn.setPrefWidth(100);
        capacityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().capacity;
            }
        });

        facilitiesColumn = new JFXTreeTableColumn<>("Facilities");
        facilitiesColumn.setPrefWidth(450);
        facilitiesColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().facilities;
            }
        });


        priceColumn = new JFXTreeTableColumn<>("Price");
        priceColumn.setPrefWidth(200);
        priceColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().price;
            }
        });

        quantityColumn = new JFXTreeTableColumn<>("Select Quantity");
        quantityColumn.setPrefWidth(200);
        quantityColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ListedRooms, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ListedRooms, String> param) {
                return param.getValue().getValue().selectedAmount;
            }
        });

        quantityColumn.setCellFactory(new Callback<TreeTableColumn<ListedRooms,String> ,TreeTableCell<ListedRooms,String> >()  {
            @Override
            public TreeTableCell<ListedRooms,String> call(TreeTableColumn<ListedRooms , String> param) {
                return new TextFieldTreeTableCell<>();
            }
        });




    }
    public void showHotelName() {


        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();
        try {
            String sql = "select hotel_name from hotel where hotel_id = ?;";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);
            statement.setInt(1 , hotelID);
            ResultSet resultSet = statement.executeQuery();

            if( resultSet.next()) {
                String name = resultSet.getString(1);
                System.out.println(name);
                hotelNameLabel.setText(name);
                hotelNameLabel.setAlignment(Pos.CENTER);

            }

            statement.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();
    }


    private void populateTable() {

        final TreeItem<ListedRooms> root = new RecursiveTreeItem<ListedRooms>(roomList, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(roomTypeColumn , capacityColumn ,facilitiesColumn , priceColumn , quantityColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }
    public void executeQuery() {



            DbAdapter dbAdapter = new DbAdapter();
            dbAdapter.connect();

            try {
                String SQL = "select *  from rooms where hotel_id = ?";

                PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
                statement.setInt(1 , hotelID);
                ResultSet result = statement.executeQuery();
                int cnt = 0;
                while( result.next()) {
                    int room_id = result.getInt(1);
                    int hotel_id = result.getInt(2);
                    String room_type = result.getString(3);
                    int price = result.getInt(4);
                    String facilities = result.getString(5);
                    int res_id = result.getInt(6);
                    int capacity = result.getInt(7);


//                    ComboBox<String> comboBox = new ComboBox<>();
//                    comboBox.getItems().addAll("0" , "1 " , "2");
//                    System.out.println("here");
                    cnt++;

                    ObservableList<String> list =   FXCollections.observableArrayList();
                    for(Integer i = 0 ;i < cnt ; i++ ) {
                        list.add(i.toString());
                    }
                    quantityColumn.setCellFactory(ComboBoxTreeTableCell.forTreeTableColumn(list));

                    roomList.add(new ListedRooms( room_type , capacity , facilities , price , "0" ));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }


            dbAdapter.disconnect();


    }

    public void setBookingClient(BookingClient bookingClient, int hotelId) {
        this.bookingClient = bookingClient;
        this.hotelID = hotelId;
        System.out.println(hotelId);

        showHotelName();
        showTreeTable();
        executeQuery();
        populateTable();
    }

    private static final class ListedRooms extends RecursiveTreeObject<ListedRooms> {
        final StringProperty roomType;
        final StringProperty capacity;
        final StringProperty facilities;
        final StringProperty price;
        final StringProperty selectedAmount;
         ComboBox<String> quantity ;


        public ListedRooms(String  roomType, Integer capacity, String facilities, Integer price, String quantity) {

            this.roomType = new SimpleStringProperty(roomType);
            this.capacity = new SimpleStringProperty(capacity.toString());
            this.facilities = new SimpleStringProperty(facilities);
            this.price = new SimpleStringProperty(price.toString());
            //this.quantity = quantity;
            this.selectedAmount = new SimpleStringProperty(quantity);
        }



    }


}
