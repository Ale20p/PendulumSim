package pendulumsim.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsScreenHandler implements Initializable {
     Stage mstage;
    public void setStage(Stage stage) {
        mstage = stage;
    }
    @FXML CheckBox periodcheck;
    @FXML CheckBox angularcheck;
    @FXML CheckBox displacementcheck;
    @FXML CheckBox velocitycheck;
    @FXML CheckBox accelerationcheck;
    @FXML CheckBox musictoggle;
    @FXML Slider volumeslider;
    Media media;
    MediaPlayer musicplayer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/pendulumsim/Data/Data.JSON"));
            JsonObject dataobj = gson.fromJson(br, JsonObject.class);
            periodcheck.setSelected(dataobj.get("Period").getAsBoolean());
            angularcheck.setSelected(dataobj.get("Angular").getAsBoolean());
            displacementcheck.setSelected(dataobj.get("Displacement").getAsBoolean());
            velocitycheck.setSelected(dataobj.get("Velocity").getAsBoolean());
            accelerationcheck.setSelected(dataobj.get("Acceleration").getAsBoolean());
            musictoggle.setSelected(dataobj.get("Music").getAsBoolean());
            volumeslider.setValue(dataobj.get("Volume").getAsDouble());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
            try {
                media = new Media(getClass().getResource("/pendulumsim/pendulumtheme.mp3").toURI().toString());
                 musicplayer = new MediaPlayer(media);

                if (musictoggle.isSelected()) {
                    musicplayer.setOnReady(() -> {
                        System.out.println("Total Duration: " + musicplayer.getTotalDuration());
                        musicplayer.play();
                        musicplayer.setAutoPlay(true);
                        musicplayer.setVolume(volumeslider.getValue()/50);
                    });
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        volumeslider.valueProperty().addListener((observable, oldValue, newValue) -> {
            musicplayer.setVolume(volumeslider.getValue()/50);
        });
    }

    public void backToStartEvent() throws IOException {
        Gson gson = new Gson();
        JsonWriter jw = new JsonWriter(new FileWriter("src/main/java/pendulumsim/Data/Data.JSON"));
        jw.beginObject();
        jw.name("Period").value(periodcheck.isSelected());
        jw.name("Angular").value(angularcheck.isSelected());
        jw.name("Displacement").value(displacementcheck.isSelected());
        jw.name("Velocity").value(velocitycheck.isSelected());
        jw.name("Acceleration").value(accelerationcheck.isSelected());
        jw.name("Music").value(musictoggle.isSelected());
        jw.name("Volume").value(volumeslider.getValue());
        jw.endObject();
        jw.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/StartingScreen.fxml"));
        Parent root = loader.load();
        StartingScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);
    }
    @FXML
    public void settingsevent() throws IOException {
        Gson gson = new Gson();
        JsonWriter jw = new JsonWriter(new FileWriter("src/main/java/pendulumsim/Data/Data.JSON"));
        jw.beginObject();
        jw.name("Period").value(periodcheck.isSelected());
        jw.name("Angular").value(angularcheck.isSelected());
        jw.name("Displacement").value(displacementcheck.isSelected());
        jw.name("Velocity").value(velocitycheck.isSelected());
        jw.name("Acceleration").value(accelerationcheck.isSelected());
        jw.name("Music").value(musictoggle.isSelected());
        jw.name("Volume").value(volumeslider.getValue());
        jw.endObject();
        jw.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/SettingsScreen.fxml"));
        Parent root = loader.load();
        SettingsScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void infoevent() throws IOException {
        Gson gson = new Gson();
        JsonWriter jw = new JsonWriter(new FileWriter("src/main/java/pendulumsim/Data/Data.JSON"));
        jw.beginObject();
        jw.name("Period").value(periodcheck.isSelected());
        jw.name("Angular").value(angularcheck.isSelected());
        jw.name("Displacement").value(displacementcheck.isSelected());
        jw.name("Velocity").value(velocitycheck.isSelected());
        jw.name("Acceleration").value(accelerationcheck.isSelected());
        jw.name("Music").value(musictoggle.isSelected());
        jw.name("Volume").value(volumeslider.getValue());
        jw.endObject();
        jw.close();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/InfoScreen.fxml"));
        Parent root = loader.load();
        InfoScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);

    }
    @FXML
    public void musiccheck(){
        if (musictoggle.isSelected()){
            musicplayer.play();
            musicplayer.setAutoPlay(true);
        } else {
            musicplayer.pause();
        }
    }
}
