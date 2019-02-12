

public class dummyClass {

    int id;

    String firstName, lastName,club;
    double rating,price;
    boolean  injuryStatus;
    String nationality,country,position;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isInjuryStatus() {
        return injuryStatus;
    }

    public void setInjuryStatus(boolean injuryStatus) {
        this.injuryStatus = injuryStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "dummyClass{" +
                "id=" + id +
                ", rating=" + rating +
                ", price=" + price +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", club='" + club + '\'' +
                ", nationality='" + nationality + '\'' +
                ", country='" + country + '\'' +
                ", position='" + position + '\'' +
                ", injuryStatus=" + injuryStatus +
                '}';
    }

    dummyClass(){}

    public dummyClass(int id, String firstName, String lastName, String club, double rating, double price, boolean injuryStatus, String nationality, String country, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.club = club;
        this.rating = rating;
        this.price = price;
        this.injuryStatus = injuryStatus;
        this.nationality = nationality;
        this.country = country;
        this.position = position;
    }
}
