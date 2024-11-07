package pendulumsim.Controller;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import pendulumsim.Model.Equations;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class StartingScreenHandler implements Initializable {
    @FXML
    Pane pendulumholder;

    Stage mstage;
    public void setStage(Stage stage) {
        mstage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        double setgrav = 9.81;
        double length = 2;
        double amplitude = 75;
        double halfperiod = Equations.calculatePeriod(length,setgrav)/2;
        int posx = (int) (pendulumholder.getPrefWidth()/2);
        Rotate rotation = new Rotate(amplitude, posx, 0);
        pendulumholder.getTransforms().add(rotation);
        KeyValue initkeyvalue = new KeyValue(rotation.angleProperty(),-75 ,Interpolator.EASE_BOTH);
        KeyFrame initkeyframe = new KeyFrame(Duration.seconds(halfperiod), initkeyvalue);
        Timeline anim = new Timeline(initkeyframe);
        anim.setAutoReverse(true);
        anim.setCycleCount(Timeline.INDEFINITE);
        anim.play();
        System.out.println("Main Screen Animation Initialized");
    }

    @FXML
    public void playevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/PendulumPhysicsScreen.fxml"));
        Parent root = loader.load();
        PPScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void settingsevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/SettingsScreen.fxml"));
        Parent root = loader.load();
        SettingsScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void infoevent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/InfoScreen.fxml"));
        Parent root = loader.load();
        InfoScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
}
