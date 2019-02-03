public class main {
    public static void main(String[] args) {

        System.out.println("Welcome to booking");

        //Connect
        BookingDbImpl bookingDb = new BookingDbImpl();
        bookingDb.connect();

        //Create Dummy Table
        bookingDb.createTable();

        bookingDb.setup();

        //Disconnect
        bookingDb.disconnect();

    }
}
