package RelationMember;

import javafx.beans.property.SimpleObjectProperty;

import java.sql.*;
//import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Gig {

    private Date date;
    private int price;

    protected Connection connection;


    //public enum event_type {Häät, Firma, Synttarit, Muu}

    private String event_type;
    private String band_name;
    private String info;

    //public enum payment_type {Käteinen, Lasku}

   // private static payment_type payment_type;
    private String address;
    private String customer_name;
    private String customer_email;
    private String customer_phone;
    private String City;
    private int gig_id;
    String payment_type;

    public Gig(Connection connection) {
        this.connection = connection;
    }


    public Gig() {}
    public Gig(Date date, int price, String event_type, String band_name, String info, String payment_type, String address,
               String customer_name, String customer_email, String customer_phone, String city) {

        this.price = price;
        this.address = address;
        this.band_name = band_name;
        this.info = info;
        this.customer_email = customer_email;
        this.customer_phone = customer_phone;
        this.City = city;
        this.customer_name = customer_name;
        this.payment_type = payment_type;
        this.event_type = event_type;
        this.date = date;


    }
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    //päivämäärän formaatti
    public DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    //public static LocalDate stringToLocalDate(String s) {
        //System.out.println(s + " _ " + LocalDate.parse(s, DateTimeFormatter.ofPattern("d.M.yyyy")));
        //return LocalDate.parse(s, DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        //return LocalDate.parse(s, DateTimeFormatter.ofPattern("d.M.yyyy"));
    //}

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setPrice(double price) {
        this.price = (int) price;
    }

    public int getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBand_name() {
        return band_name;
    }

    public void setBand_name(String band_name) {
        this.band_name = band_name;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }
    //public void setEvent_type(String event_type) {
        //this.event_type = Gig.event_type.valueOf(event_type);
    //}

    //public void setServiceType(String serviceType) { this.serviceType = ServiceType.valueOf(serviceType); }


    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }
    public String getPayment_type() {return payment_type;}

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public int getGig_id() {
        return gig_id;
    }

    public void setGig_id(int gig_id) {
        this.gig_id = gig_id;
    }

    @Override
    public String toString() {
        return "\n" + gig_id;
    }

    /**
     * Tuo ID:tä vastaavat tiedot tietokannasta Gig-oliolle
     *
     * @param gig_id, connection
     * @throws SQLException
     */

    public void loadData(Connection connection, String gig_id) throws SQLException{
        //haetaan keikan tiedot
        String query = "SELECT pvm, price, payment_type, info, band_name, event_type, address, customer_name, customer_email, customer_phone, city FROM gig WHERE gig_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, gig_id);
        ResultSet gigResultSet = preparedStatement.executeQuery();
        //tuodaan tiedot ResultSetistä
        if (gigResultSet.first()) {
            //date-päiväys
            //date = LocalDate.parse(gigResultSet.getString("pvm"), dateformat);

            setPrice(gigResultSet.getDouble("price"));
            setPayment_type(gigResultSet.getString("payment_type"));
            setInfo(gigResultSet.getString("info"));
            setBand_name(gigResultSet.getString("band_name"));
            setEvent_type(gigResultSet.getString("event_type"));
            setAddress(gigResultSet.getString("address"));
            setCustomer_name(gigResultSet.getString("customer_name"));
            setCustomer_email(gigResultSet.getString("customer_email"));
            setCustomer_phone(gigResultSet.getString("customer_phone"));
            setCity(gigResultSet.getString("city"));
            setDate(gigResultSet.getDate("pvm"));

            System.out.println("Tiedot ladattu kyseiseltä id:ltä");

        }
    }
    /**Tarkistaa, löytyykö kyseinen id tietokannasta
     * @param yhteys Connection yhteys tietokantaan josta ID:tä etsitään
     * @param ID id
     * @return boolean löytyykö tietokannasta
     * @throws SQLException
     */
    public static boolean idCheck(Connection yhteys, int ID) throws SQLException{
        String idCheck = "SELECT id FROM gig WHERE id = ?";
        PreparedStatement idCheckPS = yhteys.prepareStatement(idCheck);
        idCheckPS.setString(1, String.valueOf(ID));
        ResultSet idCheckRS = idCheckPS.executeQuery();
        if(idCheckRS.next()){
            return true;
        }
        else return false;
    }
    public void saveData(Connection connection) throws SQLException {//viedään palvelun tiedot

                String query = "UPDATE gig SET pvm = ?, price = ?, payment_type = ?, info = ?, bandname = ?, event_type = ?, address = ?, customer_name = ?, customer_email = ?, customer_phone = ?, city = ? WHERE gig_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //preparedStatement.setString(1, dateformat.format(date));
                //preparedStatement.setString(1, dateformat.format(date));
                preparedStatement.setString(1, String.valueOf(date));
                preparedStatement.setDouble(2, price);
                preparedStatement.setString(3, payment_type);
                preparedStatement.setString(4, info);
                preparedStatement.setString(5, band_name);
                preparedStatement.setString(6, event_type);
                preparedStatement.setString(7, address);
                preparedStatement.setString(8, customer_name);
                preparedStatement.setString(9, customer_email);
                preparedStatement.setString(10, customer_phone);
                preparedStatement.setString(11,City);
                preparedStatement.executeUpdate();

    }
    public void newGig(Connection connection) throws SQLException{

                String query = "INSERT INTO gig (pvm, price, payment_type, info, band_name, event_type, address, customer_name, customer_email, customer_phone, city) values (STR_TO_DATE(?, '%d.%m.%Y'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                //preparedStatement.setString(1, dateformat.format(date));
                preparedStatement.setString(1, String.valueOf(date));
                preparedStatement.setDouble(2, price);
                preparedStatement.setString(3, String.valueOf(payment_type));
                preparedStatement.setString(4, info);
                preparedStatement.setString(5, band_name);
                preparedStatement.setString(6, event_type);
                preparedStatement.setString(7, address);
                preparedStatement.setString(8, customer_name);
                preparedStatement.setString(9, customer_email);
                preparedStatement.setString(10, customer_phone);
                preparedStatement.setString(11,City);
                preparedStatement.executeUpdate();

        }

    public void deleteData() {}


}
