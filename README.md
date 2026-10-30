# Neurox Tools 
### All-in-One Android Utility Application

Neurox Tools is an Android application that brings multiple everyday utilities and mini-games together in one place. Built using **Java and XML**, the application aims to provide convenient access to useful tools through a single mobile platform.

Instead of installing separate applications for different everyday tasks, users can access a collection of utilities from Neurox Tools.

---

## Features

Neurox Tools combines 15+ everyday utilities and games.

### Utility Features

- **Calculator** – Perform everyday mathematical calculations.
- **Camera** – Access camera functionality.
- **Bluetooth** – Access Bluetooth-related device functionality.
- **Wi-Fi** – Access Wi-Fi-related functionality.
- **Compass** – Explore directional information.
- **Torch** – Control the device flashlight.
- **Vibration** – Access device vibration functionality.
- **Web Browser** – Browse web content.
- **Media Player** – Play media.
- **Video Player** – Access video playback functionality.
- **System Information** – Explore device-related information.
- **Call Utility** – Access calling functionality.

### Games & Entertainment

- Snake Game
- Tic-Tac-Toe
- Quiz
- Additional games and entertainment features

### Authentication

- Firebase Authentication
- User registration
- User login

### Device Integration

- Bluetooth integration
- Camera access
- Flashlight control
- Wi-Fi-related functionality
- Compass functionality
- Vibration support

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Application development |
| XML | UI layouts and design |
| Android Studio | Development environment |
| Firebase Authentication | User authentication |
| Firebase | Backend service integration |
| Gradle | Build automation |

---

## Project Structure

```text
NeuroxTools/
│
├── app/
│   ├── src/
│   │   ├── androidTest/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/neuroxtools/
│   │   │   │       ├── MainActivity.java
│   │   │   │       ├── Login.java
│   │   │   │       ├── Register.java
│   │   │   │       ├── Calculator.java
│   │   │   │       ├── Camera.java
│   │   │   │       ├── Bluetooth.java
│   │   │   │       ├── Compass.java
│   │   │   │       ├── Games.java
│   │   │   │       └── ...
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── layout/
│   │   │   │   ├── mipmap/
│   │   │   │   └── values/
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/
│   │
│   └── build.gradle.kts
│
├── gradle/
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

---

## Installation & Setup

Follow these steps to run Neurox Tools locally.

### 1. Clone the Repository

```bash
git clone https://github.com/Indra9555/Neurox-Tools.git
```

### 2. Open in Android Studio

1. Launch Android Studio.
2. Select **Open**.
3. Navigate to the cloned `Neurox-Tools` directory.
4. Wait for Gradle synchronization to complete.

### 3. Configure Firebase

The Firebase configuration file is intentionally excluded from this repository.

To configure Firebase:

1. Open the [Firebase Console](https://console.firebase.google.com/).
2. Create or select your Firebase project.
3. Register an Android application using the correct package name.
4. Download `google-services.json`.
5. Place the file inside the `app/` directory.
6. Configure Firebase Authentication and enable the authentication methods required by the application.

### 4. Build and Run

- Connect an Android device or start an emulator.
- Allow Gradle synchronization to finish.
- Click **Run** in Android Studio.

Alternatively, build using the Gradle wrapper:

```bash
./gradlew assembleDebug
```

On Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug
```

---

## Security & Configuration

- Firebase configuration files are excluded from version control.
- Local Android Studio configuration files are ignored.
- Build outputs and signing credentials are excluded from Git.

**Important:** Configure appropriate Firebase security rules before using the application in production.

---

## Project Objectives

- Combine multiple everyday utilities into one Android application.
- Explore Android development using Java and XML.
- Understand Firebase Authentication integration.
- Work with Android device hardware and system features.
- Develop interactive interfaces and simple games.
- Strengthen practical application development skills.

---

## Future Improvements

Potential improvements include:

- UI/UX enhancements.
- Additional everyday utilities.
- More interactive games.
- Improved application performance.
- Additional customization options.
- Enhanced security and error handling.

---

## Developer

**Indrajeet Verma**

- GitHub: [@Indra9555](https://github.com/Indra9555)
- Project: [Neurox Tools](https://github.com/Indra9555/Neurox-Tools)

---

## License

No license has currently been specified. All rights are reserved by the project owner unless a license is added.

---

<p align="center">
  <b>Neurox Tools — Multiple Utilities. One Application.</b>
</p>
