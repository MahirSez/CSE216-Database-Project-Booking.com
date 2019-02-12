package sample;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.ResourceBundle;




public class pickUpTeamController implements Initializable {
// join league class

    double balance =100;

    boolean done  = false;
    int max_def  =5, def =  0, max_mf = 5, mf = 0, fwd = 0,  max_fwd = 3, gk =0 , max_gk =2;
    int array[] = new int[1000],cap = 0, v_cap =0;
    String captain,v_captain;

    @FXML public ComboBox<String>  comboBox  = new ComboBox<String>();
    public ObservableList<String> list = FXCollections.observableArrayList("Deffender", "Midfielder","Forward","Goalkeeper");

    public ObservableList<dummyClass> list1 =FXCollections.observableArrayList();


    public ObservableList<dummyClass> list2 =FXCollections.observableArrayList();
    String selected;
///// for joinLeague class


    //public Hashtable<Integer,Boolean>



    @FXML
    void generateCombo(ActionEvent event)
    {

        selected = comboBox.getValue();
        System.out.println(selected);
        Adapter adapter = new Adapter();
        adapter.connect();

        try {
           // String sql = "SELECT * FROM player where position="+selected+";";

           // System.out.println(sql);
            list1.clear();
            adapter.stmt= adapter.conn.prepareStatement("SELECT * FROM player where position= '"+selected+"';");
            adapter.rs= ((PreparedStatement) adapter.stmt).executeQuery();

            while(adapter.rs.next())
           {
               int i=adapter.rs.getInt(1);
               String s1 = adapter.rs.getString(2);
               String  s2 =adapter.rs.getString(3);
               String s3=adapter.rs.getString(4);
               double d1= adapter.rs.getDouble(5);
               double d2 =adapter.rs.getDouble(6);
               boolean b1=adapter.rs.getBoolean(7);
               String s4 =adapter.rs.getString(8);
               String s5 =adapter.rs.getString(9);
              // dummyClass dum = new dummyClass(adapter.rs.getInt(1),adapter.rs.getString(2),adapter.rs.getString(3),adapter.rs.getString(4),adapter.rs.getDouble(5),adapter.rs.getDouble(6),adapter.rs.getBoolean(7),adapter.rs.getString(8),adapter.rs.getString(5));
              // dummyClass dum =new dummyClass(adapter.rs.getInt(1),adapter.rs.getString(2),adapter.rs.getString(3), adapter.rs.getString(4),adapter.rs.getDouble(5),adapter.rs.getDouble(6), adapter.rs.getBoolean(7),adapter.rs.getString(8),adapter.rs.getString(9));
//                System.out.println(rs.getString(1)+rs.getInt(2));
               dummyClass dum=  new dummyClass();
               dum.setId(i);
               dum.setFirstName(s1);
               dum.setLastName(s2);
               dum.setClub(s3);
               dum.setRating(d1);
               dum.setPrice(d2);
               dum.setInjuryStatus(b1);
               dum.setCountry(s4);
               dum.setPosition(s5);
               list1.add(dum);

           }
           table.setItems(list1);
           // System.out.println(list1);
            //System.out.println(table.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    private Main main;
   @FXML public TableView<dummyClass> table = new TableView<dummyClass>();
   @FXML public TableView<dummyClass> table1 = new TableView<dummyClass>();
   @FXML private Button showButton,addButton,confirmTeamButton,captainButton,vcButton;



   ///for joinleague class





    void setMain(Main main)
    {
        this.main= main;
    }
   @FXML TableColumn< dummyClass,Integer>  playerId,playerId1  ;
   @FXML TableColumn< dummyClass,String> firstName ,firstName1   ;
   @FXML TableColumn< dummyClass,String> lastName,lastName1  ;
   @FXML TableColumn< dummyClass,String> club ,club1  ;
   @FXML TableColumn< dummyClass,Double> rating ,rating1    ;
   @FXML TableColumn< dummyClass,Double>  price,price1  ;
   @FXML TableColumn< dummyClass,Boolean> injury ,injury1   ;
   @FXML TableColumn< dummyClass,String> country,country1    ;
   @FXML TableColumn< dummyClass,String> position ,position1  ;






    @Override
    public void initialize(URL location, ResourceBundle resources)
    {

        System.out.println("came here");
        System.out.println(list);
        comboBox.setItems(list);
        playerId.setCellValueFactory(new PropertyValueFactory<dummyClass,Integer>("Id"));
        firstName.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("LastName"));
        club.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("Club"));
        rating.setCellValueFactory(new PropertyValueFactory<dummyClass,Double>("Rating"));
        price.setCellValueFactory(new PropertyValueFactory<dummyClass,Double>("Price"));
        injury.setCellValueFactory(new PropertyValueFactory<dummyClass,Boolean>("InjuryStatus"));
        country.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("Country"));
        position.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("Position"));

        playerId1.setCellValueFactory(new PropertyValueFactory<dummyClass,Integer>("Id"));
        firstName1.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("firstName"));
        lastName1.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("LastName"));
        club1.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("Club"));
        rating1.setCellValueFactory(new PropertyValueFactory<dummyClass,Double>("Rating"));
        price1.setCellValueFactory(new PropertyValueFactory<dummyClass,Double>("Price"));
        injury1.setCellValueFactory(new PropertyValueFactory<dummyClass,Boolean>("InjuryStatus"));
        country1.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("Country"));
        position1.setCellValueFactory(new PropertyValueFactory<dummyClass,String>("Position"));

        table.setItems(list1);
        table1.setItems(list2);
    }


   @FXML public void showItem(ActionEvent event)
   {
       dummyClass item = table.getSelectionModel().getSelectedItem();
      // item.club;
       boolean bool =false;


       if(item.position.equals("Deffender") && def<max_def && array[item.id]==0 && balance >0 ){def++;bool = true;}
       else if(item.position.equals("Midfielder") && mf<max_mf && array[item.id]==0 && balance >0 ){mf++; bool = true;}
       else if(item.position.equals("Forward") && fwd<max_fwd && array[item.id]==0){fwd++;bool = true;}
       else if (item.position.equals("Goalkeeper") && gk<max_gk){gk++;bool = true;}
       if(bool  == true)
       {
           String s2;
           list2.add(item);
           array[item.id]=1;
//           Adapter adapter = new Adapter();
//           adapter.connect();
//           try {
//               adapter.stmt= adapter.conn.prepareStatement("SELECT * FROM fantasy_team ;");
//               adapter.rs= ((PreparedStatement) adapter.stmt).executeQuery();
//               while(adapter.rs.next())
//               {
//                   s1 = adapter.rs.getInt(1);
//                   s2 = adapter.rs.getString(5);
//                   if(s2.equals(signupcontroller.string)) { break;}
//               }
//           }catch (SQLException e)
//           {
//               e.printStackTrace();
//           }

//           String sql2="insert into team_and_player values ("+s1+"," + item.getId()+",1,8.9,false,'"+captain+"','"+v_captain+"')" ;
//           try{
//           adapter.stmt=adapter.conn.createStatement();
//
//               adapter.stmt.executeUpdate(sql2);
//           } catch (SQLException e) {
//               e.printStackTrace();
//           }

//           String sql="-1";
//           if(s1!=-1)  sql= "insert into team_and_player values ("+s1+"','"+1+"','" +0+"' ,'"+cap+"','"+v_cap+"')";
//           try {
//               if(done!=true && cap!=0 && v_cap!=0 && s1!=-1) {
//                   System.out.println(cap);
//                   System.out.println(v_cap);
//                   adapter.stmt = adapter.conn.createStatement();
//                   adapter.stmt.executeUpdate(sql);
//                   done = true;
//               }
//
//           } catch (SQLException e) {
//               e.printStackTrace();
//           }
       }


       System.out.println(item);

   }

   @FXML public void confirmTeamAction(ActionEvent event)
   {
       try {
           main.showHomePage();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   @FXML public void captainAction(ActionEvent event)
   {
       dummyClass dum;
       dum =table1.getSelectionModel().getSelectedItem();
       captain= dum.getFirstName();
       cap =1;

   }
   @FXML public void vcAction(ActionEvent event)
   {
        dummyClass dum =table1.getSelectionModel().getSelectedItem();
        v_captain =dum.getFirstName();
        v_cap =1;
   }



   ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Image image;

    {
        try {
            image = new Image(new FileInputStream("C:\\Users\\Nibir\\Downloads\\JavaFXLogin\\src\\sample\\salah.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private ImageView salah = new ImageView(image);

    public  ObservableList<joinLeague> jlist =FXCollections.observableArrayList();
    @FXML public TableView<joinLeague> tableJoin = new TableView<joinLeague>();
    @FXML private Button joinLeagueButton,createLeagueButton;


}
