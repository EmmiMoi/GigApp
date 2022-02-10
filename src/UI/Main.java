package UI;

import RelationMember.Gig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {
    //public Connection getConnection;
    private Connection connection;
    public Connection getConnection() {
        return connection;
    }

    private Gig selectedGig;

    public void setSelectedGig(Gig selectedGiggig) {
        this.selectedGig = selectedGiggig;}



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        primaryStage.setTitle("Keikat");
        primaryStage.setScene(new Scene(root, 879, 752));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
