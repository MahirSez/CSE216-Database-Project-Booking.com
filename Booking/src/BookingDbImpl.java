

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingDbImpl extends DbAdapter {

    public void createTable() {
        try {
            stmt = conn.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS products (" +
                    "product_id SERIAL PRIMARY KEY NOT NULL," +
                    "product_name TEXT NOT NULL," +
                    "product_price REAL)";
            stmt.executeUpdate(sql);
            stmt.close();

        } catch( SQLException e) {
            e.printStackTrace();
        }
    }


    public void setup() {

        int numberOfRows = -1;

        try {
            PreparedStatement st = conn.prepareStatement("SELECT count(*) FROM products");

            ResultSet rs = st.executeQuery();
            while( rs.next()) {
                numberOfRows = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if( numberOfRows ==0 ) {
            //Do Setup
            System.out.println("Number of Rows: "+ numberOfRows);
        }
    }

    public void insertProduct(String productName , Double productPrice) {



    }
}
