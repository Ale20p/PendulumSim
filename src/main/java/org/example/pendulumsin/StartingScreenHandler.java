package org.example.pendulumsin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class StartingScreenHandler implements Initializable {
    @FXML
    Rectangle string;
    @FXML Circle ball;

    Stage mstage;
    public void setStage(Stage stage) {
        mstage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void playevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PendulumPhysicsScreen.fxml"));
        Parent root = loader.load();
        PPScreenHandler pp = new PPScreenHandler();
        pp.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void settingsevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingsScreen.fxml"));
        Parent root = loader.load();
       SettingsScreenHandler ss = new SettingsScreenHandler();
        ss.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void infoevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoScreen.fxml"));
        Parent root = loader.load();
        InfoScreenHandler ish = new InfoScreenHandler();
        ish.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
}
