import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;

public class MainApp extends Application {

    private int focusTimeMinutes = 25;
    private int secondsRemaining = focusTimeMinutes * 60;
    private boolean isRunning = false;
    private String currentMode = "FOCUS";

    private Label timerLabel;
    private Label titleLabel;
    private Button startButton;
    private Timeline timeline;
    private VBox mainLayout;
    private HBox nextActionBox;
    private String defaultTextColor = "white";

    @Override
    public void start(Stage primaryStage) {
        titleLabel = new Label("🍅 FOCUS TIME");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        timerLabel = new Label(formatTime(secondsRemaining));
        timerLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: white;");


        Button focusModeBtn = createStyledButton("🍅 Focus", "#ffffff", "#2c3e50");
        Button shortBreakBtn = createStyledButton("☕ Short Break", "#ffffff", "#2c3e50");
        Button longBreakBtn = createStyledButton("🌴 Long Break", "#ffffff", "#2c3e50");

        HBox modeBox = new HBox(10, focusModeBtn, shortBreakBtn, longBreakBtn);
        modeBox.setAlignment(Pos.CENTER);


        Button t15Btn = createTimeButton("15m");
        Button t25Btn = createTimeButton("25m");
        Button t45Btn = createTimeButton("45m");
        Button t60Btn = createTimeButton("1h");

        TextField customTimeInput = new TextField();
        customTimeInput.setPromptText("Min...");
        customTimeInput.setPrefWidth(55);
        customTimeInput.setStyle("-fx-background-radius: 10; -fx-alignment: center; -fx-font-weight: bold;");

        Button setCustomBtn = createTimeButton("OK");
        setCustomBtn.setOnAction(e -> {
            try {
                int mins = Integer.parseInt(customTimeInput.getText());
                if (mins > 0 && mins <= 180) {
                    changeFocusTime(mins);
                    customTimeInput.clear();
                }
            } catch (NumberFormatException ex) {

            }
        });

        HBox timePresetBox = new HBox(8, t15Btn, t25Btn, t45Btn, t60Btn, customTimeInput, setCustomBtn);
        timePresetBox.setAlignment(Pos.CENTER);


        focusModeBtn.setOnAction(e -> setMode("FOCUS", focusTimeMinutes, "#e74c3c", "🍅 FOCUS TIME"));
        shortBreakBtn.setOnAction(e -> setMode("SHORT_BREAK", 5, "#2ecc71", "☕ SHORT BREAK"));
        longBreakBtn.setOnAction(e -> setMode("LONG_BREAK", 15, "#3498db", "🌴 LONG BREAK"));

        t15Btn.setOnAction(e -> changeFocusTime(15));
        t25Btn.setOnAction(e -> changeFocusTime(25));
        t45Btn.setOnAction(e -> changeFocusTime(45));
        t60Btn.setOnAction(e -> changeFocusTime(60));


        startButton = createStyledButton("Start", "#27ae60", "#ffffff");
        startButton.setStyle(startButton.getStyle() + " -fx-font-size: 18px; -fx-padding: 10 30;");

        Button resetButton = createStyledButton("Reset", "#95a5a6", "#ffffff");

        HBox controlBox = new HBox(15, startButton, resetButton);
        controlBox.setAlignment(Pos.CENTER);


        Button startBreakOptionBtn = createStyledButton("☕ Start Break (5m)", "#ffffff", "#2ecc71");
        Button backToFocusOptionBtn = createStyledButton("🍅 Back to Focus", "#ffffff", "#e74c3c");

        nextActionBox = new HBox(10, startBreakOptionBtn, backToFocusOptionBtn);
        nextActionBox.setAlignment(Pos.CENTER);
        nextActionBox.setVisible(false);
        nextActionBox.setManaged(false);

        startBreakOptionBtn.setOnAction(e -> {
            setMode("SHORT_BREAK", 5, "#2ecc71", "☕ SHORT BREAK");
            toggleTimer();
        });

        backToFocusOptionBtn.setOnAction(e -> {
            setMode("FOCUS", focusTimeMinutes, "#e74c3c", "🍅 FOCUS TIME");
            toggleTimer();
        });


        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsRemaining--;
            timerLabel.setText(formatTime(secondsRemaining));


            if (secondsRemaining <= 5 && secondsRemaining > 0) {
                timerLabel.setStyle("-fx-font-size: 72px; -fx-font-weight: bold; -fx-text-fill: #f1c40f;");
                playSound("/tick.mp3"); // Toca o som do tique/bip se existir
            } else {
                timerLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: " + defaultTextColor + ";");
            }


            if (secondsRemaining <= 0) {
                timeline.stop();
                isRunning = false;
                startButton.setText("Start");
                timerLabel.setText("🎉 TIME'S UP!");
                timerLabel.setStyle("-fx-font-size: 48px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");


                playSound("/alarm.mp3");


                nextActionBox.setVisible(true);
                nextActionBox.setManaged(true);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        startButton.setOnAction(e -> toggleTimer());
        resetButton.setOnAction(e -> resetTimer());


        mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #e74c3c; -fx-padding: 20;");
        mainLayout.getChildren().addAll(modeBox, timePresetBox, titleLabel, timerLabel, controlBox, nextActionBox);

        Scene scene = new Scene(mainLayout, 440, 520);
        primaryStage.setTitle("Pomodoro Studio");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void playSound(String resourcePath) {
        try {
            URL soundUrl = getClass().getResource(resourcePath);
            if (soundUrl != null) {
                AudioClip clip = new AudioClip(soundUrl.toExternalForm());
                clip.play();
            } else if (resourcePath.equals("/alarm.mp3")) {
                java.awt.Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void changeFocusTime(int minutes) {
        this.focusTimeMinutes = minutes;
        if (currentMode.equals("FOCUS")) {
            setMode("FOCUS", minutes, "#e74c3c", "🍅 FOCUS TIME (" + minutes + " min)");
        }
    }

    private void setMode(String mode, int minutes, String colorHex, String titleText) {
        timeline.stop();
        isRunning = false;
        currentMode = mode;
        secondsRemaining = minutes * 60;
        titleLabel.setText(titleText);
        timerLabel.setText(formatTime(secondsRemaining));
        timerLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: " + defaultTextColor + ";");
        startButton.setText("Start");
        mainLayout.setStyle("-fx-background-color: " + colorHex + "; -fx-padding: 20;");


        nextActionBox.setVisible(false);
        nextActionBox.setManaged(false);
    }

    private void toggleTimer() {
        nextActionBox.setVisible(false);
        nextActionBox.setManaged(false);

        if (isRunning) {
            timeline.pause();
            startButton.setText("Resume");
            isRunning = false;
        } else {
            timeline.play();
            startButton.setText("Pause");
            isRunning = true;
        }
    }

    private void resetTimer() {
        timeline.stop();
        isRunning = false;
        nextActionBox.setVisible(false);
        nextActionBox.setManaged(false);

        int initialMinutes = currentMode.equals("FOCUS") ? focusTimeMinutes : (currentMode.equals("SHORT_BREAK") ? 5 : 15);
        secondsRemaining = initialMinutes * 60;
        timerLabel.setText(formatTime(secondsRemaining));
        timerLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold; -fx-text-fill: " + defaultTextColor + ";");
        startButton.setText("Start");
    }

    private Button createStyledButton(String text, String bgColor, String textColor) {
        Button btn = new Button(text);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: %s; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 8 16;",
                bgColor, textColor
        ));
        return btn;
    }

    private Button createTimeButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-cursor: hand; -fx-padding: 4 12;");
        return btn;
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void main(String[] args) {
        launch(args);
    }
}