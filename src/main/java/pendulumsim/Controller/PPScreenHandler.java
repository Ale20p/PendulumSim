package pendulumsim.Controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import pendulumsim.Model.Equations;

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
    @FXML TextField gravityInput;
    @FXML TextField massInput;
    @FXML TextField periodInput;
    @FXML Button backButton;
    @FXML Pane pendulumHolder;
    @FXML TextField angularFrequencyInput;
    @FXML TextField velocityInput;
    @FXML TextField accelerationInput;
    @FXML TextField displacementInput;

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

    private double defaultLength = 240;
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

        gravityInput.textProperty().addListener((observable, oldValue, newValue) -> updateCalculations());
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
                string.setHeight(length);
                ball.setLayoutY(length);
            }
            if (isUpdatingFromTextInput) {
                if (!lengthInput.getText().isEmpty()) {
                    length = Double.parseDouble(lengthInput.getText());
                    lengthSlider.setValue(length); // Update slider based on input
                    string.setHeight(length);
                    ball.setLayoutY(length);
                }
            }

            // update gravity
            if (!gravityInput.getText().isEmpty()) {
                gravity = Double.parseDouble(gravityInput.getText());
            }

            // update amplitude
            if (!amplitudeInput.getText().isEmpty()) {
                amplitude = Double.parseDouble(amplitudeInput.getText());
            }

            // Parse mass
            if (!massInput.getText().isEmpty()) {
                mass = Double.parseDouble(massInput.getText());
                ball.setRadius(mass);
            }

            // Period and Angular Frequency Calculations
            if (length > 0 && gravity > 0) {
                period = Equations.calculatePeriod(length, gravity);
                periodInput.setText(String.format("%.2f", period));

                angularFrequency = Equations.calculateAngularFrequency(length, gravity);
                angularFrequencyInput.setText(String.format("%.2f", angularFrequency));

                // Velocity Calculation
                velocity = Equations.calculateVelocity(amplitude, angularFrequency, period);
                velocityInput.setText(String.format("%.2f", velocity));

                // Displacement Calculation
                displacement = Equations.calculateDisplacement(amplitude, angularFrequency, time);
                displacementInput.setText(String.format("%.2f", displacement));
            }


            // Update animation to match new parameters
            updateAnimation();

        } catch (NumberFormatException e) {
            System.out.println("Invalid input for one or more parameters.");
        }
    }

    private void updateAnimation() {
        double positionX = pendulumHolder.getPrefWidth() / 2;
        double halfPeriod = period / 2;

        Rotate rotation = new Rotate(amplitude, positionX, 0);
        pendulumHolder.getTransforms().clear();
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
    }


    public void  startEvent() {
        // Length
        if (lengthInput.getText().isEmpty() && lengthSlider.getValue() == 0.0) {
            lengthInput.setText(String.valueOf(defaultLength));
            length = Double.parseDouble(lengthInput.getText());
            string.setHeight(length);
            ball.setLayoutY(length);
        } else if (lengthInput.getText().isEmpty()) {
            lengthInput.setText(String.valueOf(lengthSlider.getValue()));
            length = lengthSlider.getValue();
            string.setHeight(length);
            ball.setLayoutY(length);
        } else {
            length = Double.parseDouble(lengthInput.getText());
            string.setHeight(length);
            ball.setLayoutY(length);
        }

        // Gravity
        if (gravityInput.getText().isEmpty()) {
            gravityInput.setText(String.valueOf(defaultGravity));
            gravity = Double.parseDouble(gravityInput.getText());
        } else {
            gravity = Double.parseDouble(gravityInput.getText());
        }

        // Amplitude
        if (amplitudeInput.getText().isEmpty()) {
            amplitudeInput.setText(String.valueOf(defaultAmplitude));
            amplitude = Double.parseDouble(amplitudeInput.getText());
        } else {
            amplitude = Double.parseDouble(amplitudeInput.getText());
        }

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
        periodInput.setText(String.valueOf(Equations.calculatePeriod(length, gravity)));
        period = Double.parseDouble(periodInput.getText());

        // Angular Frequency
        angularFrequencyInput.setText(String.valueOf(Equations.calculateAngularFrequency(length, gravity)));
        angularFrequency = Double.parseDouble(angularFrequencyInput.getText());

        animation.play();
        velocityUpdated.start();
        accelerationUpdated.start();
        displacementUpdated.start();
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


    public void pauseEvent() throws IOException {
        animation.pause();
    }

    public void resetEvent() throws IOException {

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
