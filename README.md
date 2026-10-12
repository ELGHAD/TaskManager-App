# TaskManager — Native Android Task Management App

A native Android application for creating, organizing, and tracking daily tasks. Built with Kotlin and Android Jetpack, the project follows the MVVM architecture and Repository pattern to promote maintainability, testability, and a clear separation of concerns.

<p align="left">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Architecture-MVVM-blue" alt="MVVM">
  <img src="https://img.shields.io/badge/Database-Room%20%2F%20SQLite-4479A1" alt="Room and SQLite">
  <img src="https://img.shields.io/badge/Build-Gradle%20Kotlin%20DSL-02303A?logo=gradle&logoColor=white" alt="Gradle">
  <img src="https://img.shields.io/badge/License-MIT-black.svg" alt="MIT License">
</p>

---

## Overview

TaskManager is a native Android application designed to simplify everyday task management through a clean and intuitive user interface.

Users can create, update, delete, and track tasks while maintaining their completion status. Local data persistence allows tasks to be stored on the device using Room, an abstraction layer over SQLite.

The application is structured around modern Android development principles, with a focus on separation of concerns, lifecycle-aware UI state management, and maintainable data access.

### Key Features

- **Task management:** Create, edit, and delete tasks.
- **Task tracking:** View tasks and distinguish between pending and completed items.
- **Task details:** Organize tasks using titles, descriptions, and optional dates.
- **Local persistence:** Store task data locally using Room and SQLite.
- **Intuitive interface:** Follow Material Design principles for a consistent user experience.
- **Structured architecture:** Separate UI components, presentation logic, and data access.
- **Maintainable codebase:** Use the Repository pattern to centralize data operations.

---

## Technology Stack

| Component | Technology |
|---|---|
| Platform | Android |
| Programming language | Kotlin |
| Architecture | MVVM (Model–View–ViewModel) |
| Design pattern | Repository |
| Local database | Room / SQLite |
| Android Jetpack | ViewModel, LiveData or StateFlow, Room |
| UI | XML layouts, Material Design |
| Build system | Gradle Kotlin DSL |
| Development environment | Android Studio |

---

## Architecture

TaskManager follows the MVVM architecture combined with the Repository pattern to separate responsibilities and improve maintainability.

The application is organized into four logical layers:

- **Model:** Defines the application's data structures and Room entities.
- **ViewModel:** Manages presentation logic and exposes UI state in a lifecycle-aware manner.
- **Repository:** Centralizes data access and separates the presentation layer from the underlying data source.
- **View:** Implements the user interface using Android components and XML layouts.

### Architecture Flow

```text
        User Interaction
               |
               v
        View (UI Layer)
               |
               v
           ViewModel
               |
               v
          Repository
               |
               v
          Room Database
               |
               v
          SQLite Storage
```

This separation makes the codebase easier to understand, extend, and maintain as new features are introduced.

---

## Getting Started

### Prerequisites

Before running the application, ensure that you have:

- [Android Studio](https://developer.android.com/studio)
- JDK compatible with the project's Gradle configuration
- An Android emulator or a physical Android device
- Git installed on your machine

### Installation

**1. Clone the repository**

```bash
git clone https://github.com/ELGHAD/TaskManager-App.git
cd TaskManager-App
```

**2. Open the project**

Open the cloned project directory in Android Studio.

Allow Gradle to synchronize the project and download the required dependencies.

**3. Run the application**

Select an Android emulator or connect a physical device with USB debugging enabled.

Select the application run configuration and click **Run** in Android Studio.

The required Android SDK and minimum supported Android version are defined in the project configuration.

---

## Usage

1. Launch the application on your Android device.
2. Create a task by providing a title and, optionally, a description and date.
3. Browse the task list to review your current activities.
4. Mark tasks as completed or pending as needed.
5. Edit or delete tasks when your plans change.

Task data is stored locally using the application's persistence layer.

---

## Project Structure

```text
TaskManager-App/
├── taskmanager/
│   ├── src/
│   │   └── main/
│   │       ├── java/          # Kotlin source code
│   │       ├── res/           # Android resources and XML layouts
│   │       └── AndroidManifest.xml
│   └── build.gradle.kts       # Module build configuration
├── gradle/                    # Gradle wrapper configuration
├── build.gradle.kts           # Root build configuration
├── settings.gradle.kts        # Project module configuration
├── gradlew
├── gradlew.bat
└── README.md
```

The directory structure above illustrates the main project components. Exact package names and files may vary depending on the current implementation.

---

## Engineering Principles

The project applies several principles associated with maintainable Android applications:

- **Separation of concerns:** Keep UI rendering, presentation logic, and data access responsibilities distinct.
- **Lifecycle awareness:** Use ViewModel and lifecycle-aware observable state to manage UI data.
- **Centralized data access:** Route database operations through the Repository layer.
- **Local data persistence:** Use Room to provide a structured interface to the SQLite database.
- **Extensibility:** Maintain a foundation that can accommodate additional task-management features.

---

## Potential Improvements

Possible future enhancements include:

- Scheduled reminders and notification support.
- Task prioritization and categorization.
- Search, filtering, and sorting.
- Unit tests for ViewModels and repository logic.
- Automated UI testing and continuous integration.
- Optional cloud synchronization and backup.

These are potential extensions rather than claims about currently implemented functionality.

---

## License

This project is distributed under the MIT License. See the `LICENSE` file for the applicable terms.

---

## Author

**ELGHAD**

- GitHub: [@ELGHAD](https://github.com/ELGHAD)
- Repository: [TaskManager-App](https://github.com/ELGHAD/TaskManager-App)

Contributions, suggestions, and constructive feedback are welcome.S
