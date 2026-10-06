import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    private int focusTimeMinutes = 25;
    private int secondsRemaining = focusTimeMinutes * 60;
    private boolean isRunning = false;
    private String currentMode = "FOCUS"; // FOCUS, SHORT_BREAK, LONG_BREAK

    private Label timerLabel;
    private Label titleLabel;
    private Button startButton;
    private Timeline timeline;
    private VBox mainLayout;

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

        HBox timePresetBox = new HBox(8, t15Btn, t25Btn, t45Btn, t60Btn);
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
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsRemaining--;
            timerLabel.setText(formatTime(secondsRemaining));

            if (secondsRemaining <= 0) {
                timeline.stop();
                isRunning = false;
                startButton.setText("Start");
                timerLabel.setText("🎉 TIME'S UP!");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        startButton.setOnAction(e -> toggleTimer());
        resetButton.setOnAction(e -> resetTimer());

        // 6. Layout Principal
        mainLayout = new VBox(20);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #e74c3c; -fx-padding: 20;");
        mainLayout.getChildren().addAll(modeBox, timePresetBox, titleLabel, timerLabel, controlBox);

        Scene scene = new Scene(mainLayout, 420, 480);
        primaryStage.setTitle("Pomodoro Studio");
        primaryStage.setScene(scene);
        primaryStage.show();
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
        startButton.setText("Start");
        mainLayout.setStyle("-fx-background-color: " + colorHex + "; -fx-padding: 20;");
    }

    private void toggleTimer() {
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
        int initialMinutes = currentMode.equals("FOCUS") ? focusTimeMinutes : (currentMode.equals("SHORT_BREAK") ? 5 : 15);
        secondsRemaining = initialMinutes * 60;
        timerLabel.setText(formatTime(secondsRemaining));
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
}