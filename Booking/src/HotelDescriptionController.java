import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.Stage;
import javafx.util.Callback;

import javax.xml.soap.Text;
import java.applet.Applet;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Observable;
import java.util.ResourceBundle;

public class HotelDescriptionController  {
    private BookingClient bookingClient;
    int hotelID;


    @FXML
    private Label hotelNameLabel;


    @FXML
    private JFXTextArea descriptionArea;

    @FXML
    private JFXTreeTableView<ReviewList> treeView;

    @FXML
    private JFXTreeTableColumn<ReviewList, String> clientNameColumn;

    @FXML
    private JFXTreeTableColumn<ReviewList, String> reviewDateColumn;

    @FXML
    private JFXTreeTableColumn<ReviewList, String> descriptionColumn;

    @FXML
    private JFXTreeTableColumn<ReviewList, String> ratingColumn;


    @FXML
    void addReview() {

    }

    ObservableList<ReviewList> reviewList;

    @FXML
    void bookRoomClicked() {
        try {
            bookingClient.showListedRooms();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    void homeLinkClicked( ) {

        try {
            bookingClient.showHotelCarSelectionMenu();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void tableListShow( ) {
        try {




        } catch (Exception e) {
            e.printStackTrace();
        }
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

            };

            statement.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();
    }

    public void showDescription() {

        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();
        try {
            String sql = "SELECT DESCRIPTION  \n" +
                    "\tFROM  HOTEL \n" +
                    "\tWHERE HOTEL_ID = ?;\n";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(sql);
            statement.setInt(1 , hotelID);
            ResultSet resultSet = statement.executeQuery();

            if( resultSet.next()) {
                String description = resultSet.getString(1);
                System.out.println(description);
                descriptionArea.setText(description);
                descriptionArea.setEditable(false);
            }

            statement.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
        }
        dbAdapter.disconnect();
    }

    public void showTreeTable() {


        reviewList = FXCollections.observableArrayList();

        clientNameColumn = new JFXTreeTableColumn<>("Name");
        clientNameColumn.setPrefWidth(350);
        clientNameColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReviewList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReviewList, String> param) {
                return param.getValue().getValue().name;
            }
        });




        reviewDateColumn = new JFXTreeTableColumn<>("Date");
        reviewDateColumn.setPrefWidth(150);
        reviewDateColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReviewList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReviewList, String> param) {
                return param.getValue().getValue().date;
            }
        });

        descriptionColumn = new JFXTreeTableColumn<>("Description");
        descriptionColumn.setPrefWidth(500);
        descriptionColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReviewList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReviewList, String> param) {
                return param.getValue().getValue().description;
            }
        });


        ratingColumn = new JFXTreeTableColumn<>("Rating");
        ratingColumn.setPrefWidth(150);
        ratingColumn.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<ReviewList, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<ReviewList, String> param) {
                return param.getValue().getValue().ratingString;
            }
        });



    }

    public void populateTable() {
        final TreeItem<ReviewList> root = new RecursiveTreeItem<ReviewList>(reviewList, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(clientNameColumn , reviewDateColumn , descriptionColumn , ratingColumn);
        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }


    public void executeQuery() {

        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();

        try {
            String SQL = "select * from hotel_review where hotel_id = ?";

            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setInt(1 , hotelID);
            ResultSet result = statement.executeQuery();
            while( result.next()) {
                int revId = result.getInt(1);
                int hotelID = result.getInt(2);
                int clientID = result.getInt(3);
                Date reviewDate = result.getDate(4);
                Double rating = result.getDouble(5);
                String description = result.getString(6);
                String clientName = getClientName(clientID);


                reviewList.add(new ReviewList( clientName, reviewDate ,  description ,rating ));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        dbAdapter.disconnect();

    }

    private String getClientName(int clientID) {

        DbAdapter dbAdapter = new DbAdapter();
        dbAdapter.connect();
        String name = null;
        try {

            String SQL = "select client_name from client where client_id = ?";
            PreparedStatement statement = dbAdapter.conn.prepareStatement(SQL);
            statement.setInt(1 , clientID);
            ResultSet result = statement.executeQuery();
            if( result.next() )name = result.getString(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        dbAdapter.disconnect();
        return name;
    }

    public void setBookingClient(BookingClient bookingClient , int hotelId) {
        this.bookingClient = bookingClient;
        this.hotelID = hotelId;
        showHotelName();
        showDescription();
        showTreeTable();
        executeQuery();
        populateTable();

    }


    private static final class ReviewList extends RecursiveTreeObject<ReviewList> {
        final StringProperty name;
        final StringProperty date;
        final StringProperty description;
        final StringProperty ratingString;

        public ReviewList(String name, Date date ,String description , Double rating ) {
            this.name = new SimpleStringProperty(name);
            this.date = new SimpleStringProperty(date.toString());
            this.description = new SimpleStringProperty(description);
            ratingString = new SimpleStringProperty(rating.toString());
        }
    }
}
