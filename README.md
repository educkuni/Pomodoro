# 🍅 Pomodoro Studio

A sleek, intuitive desktop Pomodoro Timer application built with **Java 17**, **JavaFX**, and **Maven**. Designed to enhance productivity through structured work and break intervals with dynamic visual feedback.

---

## ✨ Features

- **Customizable Focus Presets:** Quickly switch focus durations between 15m *(Beginner)*, 25m *(Standard)*, 45m, and 60m *(Advanced)*.
- **Break Modes:** Easily toggle between **Short Break** (5 min) and **Long Break** (15 min).
- **Dynamic UI Themes:** Responsive background colors that adjust seamlessly to reflect your current mode (Focus, Short Break, or Long Break).
- **Timer Controls:** Intuitive Start, Pause, Resume, and Reset functionalities.
- **Cross-Platform:** Runs on Windows, macOS, and Linux via JavaFX.

---

## 🛠️ Tech Stack & Prerequisites

- **Language:** Java 17+
- **GUI Framework:** JavaFX 21
- **Build Tool:** Apache Maven
- **IDE:** IntelliJ IDEA (or any Java IDE)

---

## 🚀 Getting Started

### Prerequisites

Ensure you have Java 17 (or newer) and Maven installed on your system.

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/YOUR_USERNAME/pomodoro-timer-app.git](https://github.com/YOUR_USERNAME/pomodoro-timer-app.git)
   cd pomodoro-timer-app

   mvn clean javafx:run

   Or run directly from IntelliJ IDEA:

Open the project in IntelliJ IDEA.

Run the Launcher.java file (located in src/main/java/Launcher.java).

pomodoro-timer-app/
├── src/
│   └── main/
│       └── java/
│           ├── MainApp.java    # Primary JavaFX Application UI & Logic
│           └── Launcher.java   # Main entry point to bypass JavaFX runtime issues
├── pom.xml                     # Maven project configuration & dependencies
└── README.md

---

### How to add this to your repository in IntelliJ:

1. Right-click the root folder of your project in IntelliJ.
2. Select **New** $\rightarrow$ **File**.
3. Name it **`README.md`**.
4. Paste the text above, save, and commit/push your changes to GitHub!

<ElicitationsGroup message="What would you like to do next?">
  <Elicitation label="Add an audio alarm when the countdown reaches zero" query="How do we add an audio alert sound effect in JavaFX when the Pomodoro timer hits 00:00?"/>
  <Elicitation label="Track and display completed Pomodoro session cycles" query="How do we add a daily completed cycle counter (e.g., 🍅 3/4) to track focus sessions in JavaFX?"/>
</ElicitationsGroup>
