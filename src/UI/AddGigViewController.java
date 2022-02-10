package UI;

import RelationMember.Gig;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddGigViewController extends MainViewController implements Initializable {
    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtCity;

    @FXML
    private ComboBox<String> comType;

    @FXML
    private DatePicker dateDate;

    @FXML
    private ComboBox<String> comBand;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextArea txtInfo;

    @FXML
    private RadioButton radInvoice;

    @FXML
    private ToggleGroup paymentType;

    @FXML
    private RadioButton radCash;

    /**Viittaus mainiin*/
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //alustetaan comboboxit
        comBand.getItems().addAll("Groupie Nights", "Emmi ja Tony", "Malibu Nightmare", "Metropolis", "Muu");
        comType.getItems().addAll("Häät", "Firma", "Synttärit", "Muu");

    }
    @FXML
    void btnAddClicked(ActionEvent event) throws  SQLException {
        Connection connection = SqlConnection.createConnection();

        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO gig (pvm, price, /**payment_type,*/info, band_name, event_type, address, customer_name, customer_email, customer_phone, city, payment_type) "
                        + " VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ? , ?)"
                //(STR_TO_DATE(?, '%d.%m.%Y')
        );

        ps.setString(1, dateDate.getValue().toString());
        ps.setString(2, txtPrice.getText());
        ps.setString(3, txtInfo.getText());
        ps.setString(4, comBand.getValue());
        ps.setString(5, comType.getValue());
        ps.setString(6, txtAddress.getText());
        ps.setString(7, txtCustomerName.getText());
        ps.setString(8, txtEmail.getText());
        ps.setString(9, txtCustomerPhone.getText());
        ps.setString(10,txtCity.getText());
        if (radCash.isSelected()) {
            ps.setString(11,"Käteinen");}
        else {
            ps.setString(11, "Lasku");
        }

        ps.execute();
        System.out.println("Keikka lisätty tauluun.");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Keikka lisätty");
        alert.setContentText("Keikka lisätty kalenteriin.");
        alert.show();






        //ei toimi
        //updateComingGigs();

        //System.out.println("Keikat taulu paivitetty.");








    }
}
