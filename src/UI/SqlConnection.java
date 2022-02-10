package UI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

/**Class that creates a sql-connection
 * @see Connection*/
public class SqlConnection {

    /**Opens sql-ocnnection (turha?)
     * @param palvelinosoite
     * @param palvelinSalasana
     * @return Connection conn
     * @throws SQLException*/
    public static Connection createConnection (String palvelinosoite, String palvelinSalasana) throws SQLException {
        String databaseName = "keikat";
        Connection conn = DriverManager.getConnection(palvelinosoite + palvelinSalasana);
        //valitaan käytettävä tietokanta
        PreparedStatement stmt = conn.prepareStatement("USE" + databaseName);
        stmt.executeQuery();
        return conn;

    }
    public static Connection createConnection() throws SQLException {
        String kohdetiedosto = "palvelinosoite.txt";
        String tietokantaNimi = "keikat";
        String palvelinOsoite = "";
        String palvelinSalasana = "";
        try {
            BufferedReader palvelinosoiteInput = new BufferedReader(new FileReader(kohdetiedosto));
            palvelinOsoite = palvelinosoiteInput.readLine();
            palvelinSalasana = palvelinosoiteInput.readLine();
            palvelinosoiteInput.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        Connection yhteys = DriverManager.getConnection(palvelinOsoite + palvelinSalasana);
        PreparedStatement statement = yhteys.prepareStatement("USE " + tietokantaNimi);
        statement.executeQuery();
        System.out.println(tietokantaNimi + " is being used");
        //System.out.println("Connection object created");
        return yhteys;

    }
    /**Closes sql-connection
     * @param conn
     * @throws SQLException*/
    public static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
        System.out.println("Connection closed");
    }

    //tulevien keikkojen  valinta taulukkoon
    public static ResultSet comingGigs(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT gig_id, pvm, band_name, city FROM gig ORDER BY gig_id"
        );

        return rs;
    }
    //keikkojen tiedot tekstikenttiin
    public static ResultSet comingInfo(Connection conn, String gig_id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs  = stmt.executeQuery(
                "SELECT pvm, price, payment_type, info, band_name, event_type, address, customer_name, customer_email, customer_phone, city FROM gig WHERE gig_id = " + gig_id

        );

        return rs;
    }




}
