
import java.sql.*;

public class DbAdapter {

    /* Variables*/
    String jdbUrl = "jdbc:postgresql://localhost:5432/postgres";
    String username = "postgres";
    String password = "123456789";

    /* Database Variables*/
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;

    /* Constructors*/

    public DbAdapter() {

    }

    /**
     *
     * Connect To Database
     */

    public void connect() {
        try {
            // connect to a database
            conn = DriverManager.getConnection(jdbUrl, username, password);

            //print success
//            System.out.println("Database Connection Established");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    } //connect


    public void disconnect () {
        try {

            if( stmt != null) {
                stmt.close();
            }

            if( rs != null) {
                rs.close();
            }

            if( conn != null) {
                conn.close();
            }


        } catch (Exception e) {
            e.printStackTrace();;
        }
    }
}
