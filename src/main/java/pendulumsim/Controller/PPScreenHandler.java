package pendulumsim.Controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javafx.animation.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import pendulumsim.Model.Equations;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PPScreenHandler implements Initializable {
    private Stage mstage;
    @FXML AnchorPane PPSpane;
    @FXML Rectangle string;
    @FXML Circle ball;
    @FXML TextField lengthInput;
    @FXML Slider lengthSlider;
    @FXML TextField amplitudeInput;
    @FXML ComboBox<String> gravityInput;
    @FXML TextField massInput;
    @FXML TextField periodInput;
    @FXML Button backButton;
    @FXML Pane pendulumHolder;
    @FXML TextField angularFrequencyInput;
    @FXML TextField velocityInput;
    @FXML TextField accelerationInput;
    @FXML TextField displacementInput;
    @FXML Pane paneperiod;
    @FXML Pane paneangular;
    @FXML Pane panedisplacement;
    @FXML Pane panevelocity;
    @FXML Pane paneacceleration;
    @FXML VBox displaybox;
    @FXML Text dispvaluetext;
    @FXML TextField mebox;
    @FXML TextField kebox;
    @FXML TextField pebox;
    @FXML Rectangle kebar;
    @FXML Rectangle pebar;

    private double length;
    private double gravity;
    private double amplitude;
    private double mass;
    private double angularFrequency;
    private double period;
    private double velocity;
    private double acceleration;
    private Timeline animation;
    private double displacement;
    private double time;
    private JsonObject dataobj;
    private int displayvaluecount = 5;
    private double kineticEnergy;
    private double potentialEnergy;
    private Timeline kineticanim;
    private Timeline potentialanim;

    private double amplitudeRadians;
    private double mechanicalenergy;


    private double defaultLength = 15;
    private double defaultGravity = 9.81;
    private double defaultMass = 45;
    private double defaultAmplitude = 75;
    private double placeholderAmplitude = 0;

    private boolean isUpdatingFromSlider = false;
    private boolean isUpdatingFromTextInput = false;

    public void setStage(Stage stage) {
        mstage = stage;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        length = defaultLength;
        gravity = defaultGravity;
        mass = defaultMass;
        amplitude = placeholderAmplitude;
        angularFrequency = Equations.calculateAngularFrequency(length,gravity);
        period = Equations.calculatePeriod(length,gravity);
        double tempvelocity = Equations.calculateVelocity(defaultAmplitude, angularFrequency,period/4);
        mechanicalenergy = Equations.calculateKineticEnergy(mass/1000,tempvelocity);
        potentialEnergy = mechanicalenergy;
        double roundedmech = Math.round(mechanicalenergy);
        mebox.setText(String.format("%.2f",roundedmech));
        pebox.setText(String.format("%.2f",roundedmech));
        kebox.setText("0");
        kebar.setHeight(0);
        kebar.setLayoutY(356);
        pebar.setHeight(218);
        pebar.setLayoutY(138);

        //removes any element not used
        Gson gson = new Gson();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/main/java/pendulumsim/Data/Data.JSON"));
            dataobj = gson.fromJson(br, JsonObject.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!dataobj.get("Period").getAsBoolean()) {
            displayvaluecount -= 1;
            displaybox.getChildren().remove(paneperiod);
        }
        if (!dataobj.get("Angular").getAsBoolean()) {
            displayvaluecount -= 1;
            displaybox.getChildren().remove(paneangular);
        }
        if (!dataobj.get("Displacement").getAsBoolean()) {
            displayvaluecount -= 1;
            displaybox.getChildren().remove(panedisplacement);
        }
        if (!dataobj.get("Velocity").getAsBoolean()) {
            displayvaluecount -= 1;
            displaybox.getChildren().remove(panevelocity);
        }
        if (!dataobj.get("Acceleration").getAsBoolean()) {
            displayvaluecount -= 1;
            displaybox.getChildren().remove(paneacceleration);
        }
        dispvaluetext.setText("Diplayed Vaules ("+ displayvaluecount+"):");

        gravityInput.getItems().addAll(
                "Earth: 9.81",
                "Moon: 1.62",
                "Mars: 3.71",
                "Jupiter: 24.79",
                "Venus: 8.87",
                "Mercury: 3.70"
        );
        gravityInput.setValue("Earth: 9.81"); // Set default value


        // Listeners
        lengthInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!isUpdatingFromSlider) { // Only update if the change is not from the slider
                isUpdatingFromTextInput = true;
                updateCalculations();
                isUpdatingFromTextInput = false;
            }
        });
        lengthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!isUpdatingFromTextInput) { // Only update if the change is not from the text input
                isUpdatingFromSlider = true;
                updateCalculations();
                isUpdatingFromSlider = false;
            }
        });
        gravityInput.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String[] parts = newValue.split(": ");
                gravity = Double.parseDouble(parts[1]);
                updateCalculations();
            }
        });
        amplitudeInput.textProperty().addListener((observable, oldValue, newValue) -> updateCalculations());
        massInput.textProperty().addListener((observable, oldValue, newValue) -> updateCalculations());

        int positionX = (int) (pendulumHolder.getPrefWidth()/2);
        double halfPeriod = Equations.calculatePeriod(length, gravity) / 2;
        
        Rotate rotation = new Rotate(amplitude, positionX, 0);
        pendulumHolder.getTransforms().add(rotation);

        KeyValue initkeyvalue = new KeyValue(rotation.angleProperty(),-75 , Interpolator.LINEAR);
        KeyFrame initkeyframe = new KeyFrame(Duration.seconds(halfPeriod), initkeyvalue);
        animation = new Timeline(initkeyframe);

        animation.setAutoReverse(true);
        animation.setCycleCount(Timeline.INDEFINITE);

        KeyValue kinetichi =  new KeyValue(kebar.heightProperty(), 0);
        KeyValue kineticpi =  new KeyValue(kebar.layoutYProperty(), 356);
        KeyFrame kintecfi = new KeyFrame(Duration.ZERO, kinetichi,kineticpi);
        KeyValue kinetichf =  new KeyValue(kebar.heightProperty(), 218);
        KeyValue kineticpf =  new KeyValue(kebar.layoutYProperty(), 138);
        KeyFrame kineticff = new KeyFrame(Duration.seconds(period/4), kinetichf,kineticpf);
        kineticanim = new Timeline(kintecfi, kineticff);
        kineticanim.setAutoReverse(true);
        kineticanim.setCycleCount(Timeline.INDEFINITE);


        KeyValue potentialhi =  new KeyValue(pebar.heightProperty(), 218);
        KeyValue potentialpi =  new KeyValue(pebar.layoutYProperty(), 138);
        KeyFrame potentialfi = new KeyFrame(Duration.ZERO, potentialhi,potentialpi);
        KeyValue potentialhf = new KeyValue(pebar.heightProperty(), 0);
        KeyValue potentialpf = new KeyValue(pebar.layoutYProperty(), 356);
        KeyFrame potentialff = new KeyFrame(Duration.seconds(period/4), potentialhf,potentialpf);
        potentialanim = new Timeline(potentialfi, potentialff);
        potentialanim.setAutoReverse(true);
        potentialanim.setCycleCount(Timeline.INDEFINITE);

        System.out.println("Pendulum Physics Screen initialized");
    }

    public void backToStartEvent() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pendulumsim/StartingScreen.fxml"));
        Parent root = loader.load();
        StartingScreenHandler controller = loader.getController();
        controller.setStage(mstage);
        Scene scene = new Scene(root);
        mstage.setScene(scene);
    }

    private void updateCalculations() {
        try {
            if (isUpdatingFromSlider) {
                length = lengthSlider.getValue();
                lengthInput.setText(String.valueOf(length)); // Update text input based on slider
                string.setHeight(length*20);
                ball.setLayoutY(length*20);
            }
            if (isUpdatingFromTextInput) {
                if (!lengthInput.getText().isEmpty()) {
                    length = Double.parseDouble(lengthInput.getText());
                    if (length > lengthSlider.getMax()) {
                        length = lengthSlider.getMax();
                        lengthInput.setText(String.valueOf(lengthSlider.getMax())); // Correct the input text if it exceeds the limit
                    }
                    lengthSlider.setValue(length); // Update slider based on input
                    string.setHeight(length*20);
                    ball.setLayoutY(length*20);
                }
            }

            // update gravity
            String selectedGravity = gravityInput.getValue();
            if (selectedGravity != null) {
                String[] parts = selectedGravity.split(": ");
                gravity = Double.parseDouble(parts[1]);
            }

            // update amplitude
            if (!amplitudeInput.getText().isEmpty()) {
                amplitude = Double.parseDouble(amplitudeInput.getText());
                if (amplitude > 100) {
                    amplitude = 100;
                    amplitudeInput.setText("100");
                }
            }
            amplitudeRadians = Math.toRadians(amplitude);

            // Parse mass
            if (!massInput.getText().isEmpty()) {
                mass = Double.parseDouble(massInput.getText());
                if (mass > 70) {
                    ball.setRadius(70.0);
                } else if (mass < 20.0) {
                    ball.setRadius(20.0);
                } else {
                    ball.setRadius(mass);
                }
            }

            // Period and Angular Frequency Calculations
            if (length > 0 && gravity > 0) {
                if (dataobj.get("Period").getAsBoolean()) {
                    periodInput.setText(String.format("%.2f", period));
                }
                period = Equations.calculatePeriod(length, gravity);

                if(dataobj.get("Angular").getAsBoolean()) {
                    angularFrequencyInput.setText(String.format("%.2f", angularFrequency));
                }
                angularFrequency = Equations.calculateAngularFrequency(length, gravity);

                // Velocity Calculation
                if (dataobj.get("Velocity").getAsBoolean()) {
                    velocityInput.setText(String.format("%.2f", velocity));
                }
                velocity = Equations.calculateVelocity(amplitude, angularFrequency, period);

                // Displacement Calculation
                if(dataobj.get("Displacement").getAsBoolean()) {
                    displacementInput.setText(String.format("%.2f", displacement));
                }
                displacement = Equations.calculateDisplacement(amplitude, angularFrequency, time);
            }

            // Update Energy
            double tempvelocity = Equations.calculateVelocity(amplitude, angularFrequency,period/4);
            mechanicalenergy = Math.round(Equations.calculateKineticEnergy(mass/1000,tempvelocity));
            potentialEnergy = mechanicalenergy;
            double roundedmech = Math.round(mechanicalenergy);
            mebox.setText(String.format("%.2f",roundedmech));
            pebox.setText(String.format("%.2f",roundedmech));
            kebox.setText("0");

            // Update animation to match new parameters
            updateAnimation();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input for one or more parameters.");
        }
    }

    private void updateAnimation() {
        double positionX = pendulumHolder.getPrefWidth() / 2;
        double halfPeriod = period / 2;


        pendulumHolder.getTransforms().clear();
        Rotate rotation = new Rotate(0, positionX, 0);
        pendulumHolder.getTransforms().add(rotation);

        KeyValue startKeyValue = new KeyValue(rotation.angleProperty(), -amplitude, Interpolator.LINEAR);
        KeyValue endKeyValue = new KeyValue(rotation.angleProperty(), amplitude, Interpolator.LINEAR);

        KeyFrame startFrame = new KeyFrame(Duration.ZERO, startKeyValue);
        KeyFrame endFrame = new KeyFrame(Duration.seconds(halfPeriod), endKeyValue);

        animation.stop();
        animation.getKeyFrames().clear();
        animation.getKeyFrames().addAll(startFrame, endFrame);
        animation.setAutoReverse(true);
        animation.setCycleCount(Timeline.INDEFINITE);

        KeyValue kinetichi =  new KeyValue(kebar.heightProperty(), 0);
        KeyValue kineticpi =  new KeyValue(kebar.layoutYProperty(), 356);
        KeyFrame kineticfi = new KeyFrame(Duration.ZERO, kinetichi,kineticpi);
        KeyValue kinetichf =  new KeyValue(kebar.heightProperty(), 218);
        KeyValue kineticpf =  new KeyValue(kebar.layoutYProperty(), 138);
        KeyFrame kineticff = new KeyFrame(Duration.seconds(period/4), kinetichf,kineticpf);
        kineticanim.stop();
        kineticanim.getKeyFrames().clear();
        kineticanim.getKeyFrames().addAll(kineticfi,kineticff);
        kineticanim.setAutoReverse(true);
        kineticanim.setCycleCount(Timeline.INDEFINITE);


        KeyValue potentialhi =  new KeyValue(pebar.heightProperty(), 218);
        KeyValue potentialpi =  new KeyValue(pebar.layoutYProperty(), 138);
        KeyFrame potentialfi = new KeyFrame(Duration.ZERO, potentialhi,potentialpi);
        KeyValue potentialhf = new KeyValue(pebar.heightProperty(), 0);
        KeyValue potentialpf = new KeyValue(pebar.layoutYProperty(), 356);
        KeyFrame potentialff = new KeyFrame(Duration.seconds(period/4), potentialhf,potentialpf);
        potentialanim.stop();
        potentialanim.getKeyFrames().clear();
        potentialanim.getKeyFrames().addAll(potentialfi,potentialff);
        potentialanim.setAutoReverse(true);
        potentialanim.setCycleCount(Timeline.INDEFINITE);


    }


    public void  startEvent() {
        // Length
        if (lengthInput.getText().isEmpty() && lengthSlider.getValue() == 0.0) {
            lengthInput.setText(String.valueOf(defaultLength));
            length = Double.parseDouble(lengthInput.getText());
            string.setHeight(length*20);
            ball.setLayoutY(length*20);
        } else if (lengthInput.getText().isEmpty()) {
            lengthInput.setText(String.format("%.2f", lengthSlider.getValue()));
            length = lengthSlider.getValue();
            string.setHeight(length*20);
            ball.setLayoutY(length*20);
        } else {
            length = Double.parseDouble(lengthInput.getText());
            if (length > lengthSlider.getMax()) {
                length = lengthSlider.getMax();
                lengthInput.setText(String.valueOf(lengthSlider.getMax())); // Correct the input text if it exceeds the limit
            }
            string.setHeight(length*20);
            ball.setLayoutY(length*20);
        }

        // Gravity
        String selectedGravity = gravityInput.getValue();
        if (selectedGravity != null) {
            String[] parts = selectedGravity.split(": ");
            gravity = Double.parseDouble(parts[1]);
        } else {
            gravity = defaultGravity;
        }

        // Amplitude
        if (amplitudeInput.getText().isEmpty()) {
            amplitudeInput.setText(String.valueOf(defaultAmplitude));
            amplitude = Double.parseDouble(amplitudeInput.getText());
        } else {
            amplitude = Double.parseDouble(amplitudeInput.getText());
        }
        if (amplitude > 100) {
            amplitude = 100;
            amplitudeInput.setText("100.00");
        }
        amplitudeRadians = Math.toRadians(amplitude);

        // Mass
        if (massInput.getText().isEmpty()) {
            massInput.setText(String.valueOf(defaultMass));
            mass = Double.parseDouble(massInput.getText());
            ball.setRadius(mass);
        } else {
            mass = Double.parseDouble(massInput.getText());
            if (mass > 70.0) {
                ball.setRadius(70.0);
            } else if (mass < 25.0) {
                ball.setRadius(25.0);
            } else {
                ball.setRadius(mass);
            }
        }

        // Period
        if (dataobj.get("Period").getAsBoolean()) {
            periodInput.setText(String.valueOf(Equations.calculatePeriod(length, gravity)));
        }

        // Angular Frequency
        if (dataobj.get("Angular").getAsBoolean()) {
            angularFrequencyInput.setText(String.valueOf(Equations.calculateAngularFrequency(length, gravity)));
        }

        animation.play();
        energyUpdated.start();
        kineticanim.play();
        potentialanim.play();
        if (dataobj.get("Velocity").getAsBoolean()) {
            velocityUpdated.start();
        }
        if (dataobj.get("Acceleration").getAsBoolean()) {
            accelerationUpdated.start();
        }
        if (dataobj.get("Displacement").getAsBoolean()) {
            displacementUpdated.start();
        }
    }

    private AnimationTimer velocityUpdated = new AnimationTimer() {
        @Override
        public void handle(long now) {
            time = animation.getCurrentTime().toSeconds();
            velocity = Equations.calculateVelocity(amplitude, angularFrequency, time);
            velocityInput.setText(String.format("%.2f", Math.abs(velocity)));
        }
    };

    private AnimationTimer accelerationUpdated = new AnimationTimer() {
        @Override
        public void handle(long l) {
            time = animation.getCurrentTime().toSeconds();
            acceleration = Equations.calculateAcceleration(amplitude, angularFrequency, time);
            accelerationInput.setText(String.format("%.2f", Math.abs(acceleration)));
        }
    };

    private AnimationTimer displacementUpdated = new AnimationTimer() {
        @Override
        public void handle(long l) {
            time = animation.getCurrentTime().toSeconds();
            displacement = Equations.calculateDisplacement(amplitude, angularFrequency, time);
            displacementInput.setText(String.format("%.2f", Math.abs(displacement)));
        }
    };

    private AnimationTimer energyUpdated = new AnimationTimer() {
        @Override
        public void handle(long l) {
            kineticEnergy = Equations.calculateKineticEnergy(mass/1000, velocity);
            potentialEnergy = mechanicalenergy - kineticEnergy;
            kebox.setText(String.format("%.2f", kineticEnergy));
            pebox.setText(String.format("%.2f", potentialEnergy));
        }
    };

    public void pauseEvent() throws IOException {
        animation.pause();
        kineticanim.pause();
        potentialanim.pause();
        velocityUpdated.stop();
        accelerationUpdated.stop();
        displacementUpdated.stop();
        time = 0;
    }

    public void resetEvent() throws IOException {
        animation.stop();
        velocityUpdated.stop();
        accelerationUpdated.stop();
        displacementUpdated.stop();
        kineticanim.stop();
        potentialanim.stop();
        time = 0;

        length = defaultLength;
        gravity = defaultGravity;
        amplitude = defaultAmplitude;
        amplitudeRadians = Math.toRadians(amplitude);
        mass = defaultMass;

        kebar.setHeight(0);
        kebar.setLayoutY(356);
        pebar.setHeight(218);
        pebar.setLayoutY(138);

        lengthInput.setText(String.valueOf(defaultLength));
        gravityInput.setValue("Earth: 9.81");
        amplitudeInput.setText(String.valueOf(defaultAmplitude));
        massInput.setText(String.valueOf(defaultMass));
        lengthSlider.setValue(defaultLength);

        period = Equations.calculatePeriod(length, gravity);
        angularFrequency = Equations.calculateAngularFrequency(length, gravity);
        velocity = Equations.calculateVelocity(amplitude, angularFrequency, time);
        acceleration = Equations.calculateAcceleration(amplitude, angularFrequency, time);
        displacement = Equations.calculateDisplacement(amplitude, angularFrequency, time);


        double tempvelocity = Equations.calculateVelocity(amplitude, angularFrequency,period/4);
        mechanicalenergy = Equations.calculateKineticEnergy(mass/1000,tempvelocity);
        potentialEnergy = mechanicalenergy;
        mebox.setText(String.format("%.2f",mechanicalenergy));
        pebox.setText(String.format("%.2f",mechanicalenergy));
        kebox.setText("0");

//        kineticEnergy = Equations.calculateKineticEnergy(mass, length, amplitudeRadians, angularFrequency, time);
//        potentialEnergy = Equations.calculatePotentialEnergy(mass, length, amplitudeRadians, angularFrequency, time, gravity);

        if (dataobj.get("Period").getAsBoolean()) {
            periodInput.setText(String.format("%.2f", period));
        }
        if (dataobj.get("Angular").getAsBoolean()) {
            angularFrequencyInput.setText(String.format("%.2f", angularFrequency));
        }
        if (dataobj.get("Velocity").getAsBoolean()) {
            velocityInput.setText(String.format("%.2f", Math.abs(velocity)));
        }
        if (dataobj.get("Acceleration").getAsBoolean()) {
            accelerationInput.setText(String.format("%.2f", Math.abs(acceleration)));
        }
        if (dataobj.get("Displacement").getAsBoolean()) {
            displacementInput.setText(String.format("%.2f", Math.abs(displacement)));
        }

        string.setHeight(length*20);
        ball.setLayoutY(length*20);
        ball.setRadius(mass);

        pendulumHolder.getTransforms().clear();
        Rotate rotation = new Rotate(defaultAmplitude, pendulumHolder.getWidth() / 2, 0);
        pendulumHolder.getTransforms().add(rotation);

        updateAnimation();
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
