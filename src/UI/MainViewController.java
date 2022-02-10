package UI;

import RelationMember.Gig;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainViewController extends Gig implements Initializable {
    public Tab tabPast;
    public Tab tabComing;
    public Label lblId;
    public Pane pane;
    @FXML
    private TableView<Gig> tblComing;
    @FXML
    private TableColumn<Gig, Integer> comingId;
    @FXML
    private TableColumn<Gig, Date> ComingDate;

    @FXML
    private TableColumn<Gig, String> ComindBand;

    @FXML
    private TableColumn<Gig, String> ComingCity;

    @FXML
    private TableView<Gig> tblPast;

    @FXML
    private TableColumn<Gig, Integer> pastId;

    @FXML
    private TableColumn<Gig, Date> pastDate;

    @FXML
    private TableColumn<Gig, String> pastBand;

    @FXML
    private TableColumn<Gig, String> pastCity;

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
    private RadioButton radInvoice;

    @FXML
    private ToggleGroup paymentType;

    @FXML
    private RadioButton radCash;

    @FXML
    private TextArea txtInfo;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtCustomerPhone;

    @FXML
    private TextField txtEmail;
    /**Viittaus mainiin*/
    private Main main;

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private CheckBox chkDone;
    private final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ofPattern("d.M.yyyy");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //päivitetään taulut
        try {
            updateComingGigs();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //alustetaan comboboxit
        comBand.getItems().addAll("Groupie Nights", "Emmi ja Tony", "Malibu Nightmare", "Metropolis", "Muu");
        comType.getItems().addAll("Häät", "Firma", "Synttärit", "Muu");

    }

    //päivittää tulevat keikat taulun
    public void updateComingGigs() throws SQLException {
        tblComing.getItems().clear();
        comingId.setCellValueFactory(new PropertyValueFactory<>("gig_id"));
        ComingDate.setCellValueFactory(new PropertyValueFactory<>("date")); //HUOM TÄMÄ OLTAVA DATE ? eli eri kuin sql

        ComindBand.setCellValueFactory(new PropertyValueFactory<>("band_name"));
        ComingCity.setCellValueFactory(new PropertyValueFactory<>("City"));

        Connection connection = SqlConnection.createConnection();

        ResultSet rs = SqlConnection.comingGigs(connection);

        // Lisätään uudet keikat TableView komponenttiin
        while (rs.next()) {

            //date = LocalDate.parse(gigResultSet.getString("pvm"), dateformat)
            //columnlabel sama nimi kuin sql-syntaksissa
            Gig gig = new Gig();
            gig.setGig_id(rs.getInt("gig_id"));
            gig.setDate(rs.getDate("pvm"));
            gig.setBand_name(rs.getString("band_name"));
            gig.setCity(rs.getString("city"));

            ///Gig gig= new Gig(comingGigs.getInt("gig_id"), comingGigs.getString("pvm"), comingGigs.getString("band_name"), comingGigs.getString("city"));
            tblComing.getItems().add(gig);

        }


    }
    public void selectGig() {




    }
    //Tallennus-painike
    public void btnSaveClicked(ActionEvent actionEvent) {

        String id = lblId.getText();
        //muutetaan id String-tyypiksi
        //String id = String.valueOf(selectedItem.getGig_id());

        try {
            saveChanges(id );
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    //keikan poisto-painike
    public void btnDeleteClicked(ActionEvent actionEvent) {
        String id = lblId.getText();
        //muutetaan id String-tyypiksi
        //String id = String.valueOf(selectedItem.getGig_id());
        //kysytään käyttäjätä haluaako poistaa keikan

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Poista keikka");
        alert.setHeaderText("Poistetaanko keikka?");
        alert.setContentText("Valitse.");

        ButtonType buttonTypeOne = new ButtonType("Poista");
        ButtonType buttonTypeCancel = new ButtonType("Peruuta", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeOne){
            try {
                deleteGig(id );
                clearFields();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else return;

    }

    //uuden keikan lisäys-painike
    public void btnAddGigClicked(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AddGigView.fxml"));
        fxmlLoader.setControllerFactory(clazz -> {
            Object controller;
            try {
                controller = clazz.getConstructor().newInstance();
            } catch (ReflectiveOperationException ex) {
                throw new RuntimeException(ex);
            }
            if (controller instanceof AddGigViewController)
                ((AddGigViewController) controller).setMain(main);
            return controller;
        });
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Lisää uusi keikka");
        stage.show();
    }

    public void tabComingClicked(Event event) {
    }

    public void tabPastClicked(Event event) {
    }

    //Metodi joka hakee keikan tiedot kenttiin
    public void tableComincClicked(MouseEvent mouseEvent) throws SQLException {

        if (tabComing.isSelected()) {
            Gig selectedItem = tblComing.getSelectionModel().getSelectedItem();
            //Gig selectedItem = tblComing.getItems().get(tblComing.getSelectionModel().getSelectedIndex());

            //indeksi päiväystä varten?
            int gigIndex = -1;
            gigIndex = tblComing.getSelectionModel().getSelectedIndex();
            if (gigIndex <= -1) {
                return;}

            //muutetaan id String-tyypiksi
            String id = String.valueOf(selectedItem.getGig_id());

            //nämä kolme onnistuu, koska ovat taulukossa
            txtCity.setText(selectedItem.getCity());
            comBand.setValue(selectedItem.getBand_name());
            comType.setValue(selectedItem.getEvent_type());
            lblId.setText(id);

            //dateCreditStudent.setValue(columnDateStudent.getCellData(courseIndx).toLocalDate());
            dateDate.setValue(ComingDate.getCellData(gigIndex).toLocalDate()); //TÄMÄ TOIMII!



            Connection connection = SqlConnection.createConnection();

            //kysely, joka hakee kaikki keikan tiedot
            ResultSet rs = SqlConnection.comingInfo(connection, id);
            if (rs.next()) {

            dateDate.setUserData(rs.getDate("pvm")); //ei toimi? onko välttämätön edes??
            txtAddress.setText(rs.getString("address"));
            txtCustomerName.setText(rs.getString("customer_name"));
            txtEmail.setText(rs.getString("customer_email"));
            txtCustomerPhone.setText(rs.getString("customer_phone"));
            txtInfo.setText(rs.getString("info"));
            comType.setValue(rs.getString("event_type"));
            txtPrice.setText(rs.getString("price"));

            if (rs.getString("payment_type").equals("Käteinen")) {
                radCash.setSelected(true);
            }
            else if (rs.getString("payment_type").equals("Lasku")) {
                radInvoice.setSelected(true);
            }

            }
        }

    }
    //tietojen päivitys
    public void saveChanges(String gig_id) throws SQLException {
        try {
            Connection connection = SqlConnection.createConnection();
            String sql = "UPDATE Gig " +
                    "SET pvm = ?  , address = ?, city = ?, band_name = ?, event_type = ?, customer_name = ?, customer_phone = ?, customer_email = ?, info = ?, price = ?, payment_type = ?" +
                    "WHERE gig_id = " + gig_id;
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, dateDate.getValue().toString());
            ps.setString(2, txtAddress.getText());
            ps.setString(3, txtCity.getText());
            ps.setString(4, comBand.getValue());
            ps.setString(5, comType.getValue());
            ps.setString(6, txtCustomerName.getText());
            ps.setString(7, txtCustomerPhone.getText());
            ps.setString(8, txtEmail.getText());
            ps.setString(9, txtInfo.getText());
            ps.setString(10,txtPrice.getText());
            //ps.setString(11, paymentType.getUserData().toString()); //ei toimi?
            if (radCash.isSelected()) {
                ps.setString(11,"Käteinen");}
            else {
                ps.setString(11, "Lasku");
            }


            //kenen tieto päivitetään
            //ps.setString(6, columnStudentidStudent.getCellData(tableStudent.getSelectionModel().getSelectedIndex()).toString());

            ps.execute();
            System.out.println("Tietoja muokattu");
            updateComingGigs();
            System.out.println("Muokattu ja päivitetty");
        } catch (SQLException ex){
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("erroria pukkaa");
            }
        }

    public void btnRefreshClicked(ActionEvent actionEvent) throws SQLException {
        updateComingGigs();
        clearFields();
    }

    public void deleteGig(String gig_id) throws SQLException {


        try {
            Connection connection = SqlConnection.createConnection();

            String sql = "DELETE FROM Gig " +
                    "WHERE gig_id =  + ? ";

            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, lblId.getText());

            ps.execute();

            System.out.println("Keikka poistettu");
            updateComingGigs();
            System.out.println("Taulu päivitetty");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void clearFields() {
        txtPrice.clear();
        dateDate.setUserData(null);
        txtAddress.clear();
        txtCity.clear();
        comBand.setValue(comBand.getPromptText());
        comType.setValue(comType.getPromptText());
        txtCustomerName.clear();
        txtCustomerPhone.clear();
        txtEmail.clear();
        txtInfo.clear();
        //paymentType



    }
}
