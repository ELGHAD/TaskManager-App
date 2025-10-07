# 📝 TaskManager – Android To-Do App

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84)]()
[![Kotlin](https://img.shields.io/badge/Kotlin-1.x-7F52FF)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-black.svg)]()
[![Build](https://img.shields.io/badge/Gradle-Kotlin%20DSL-02303A)]()

**TaskManager** is a modern Android application that helps users create, organize, and track daily tasks with a clean, intuitive UI.  
Built with **Kotlin**, **Android Jetpack**, and a **clean MVVM architecture** for scalability and testability.

---

## ✨ Features
- ✅ Create, edit, delete tasks
- 📋 View all tasks in a clean list
- 🕓 Mark tasks as completed/pending
- 🔔 *(Optional)* Reminders & notifications
- 💾 Local persistence with Room (SQLite)
- 🎨 Material Design & smooth UX

---

## 🧠 Architecture
TaskManager follows **MVVM** with a repository pattern and Android Jetpack components:

- **Model**: Data classes / Room entities  
- **ViewModel**: Lifecycle-aware state holder for the UI  
- **Repository**: Single source of truth for data access  
- **View (UI)**: Activities/Fragments + RecyclerView adapters

---

## 🛠 Tech Stack
- **Language:** Kotlin  
- **Architecture:** MVVM + Repository  
- **Database:** Room (SQLite)  
- **Jetpack:** ViewModel, LiveData (or StateFlow), Room  
- **UI:** XML layouts, Material Design  
- **Build:** Gradle (Kotlin DSL)  
- **IDE:** Android Studio

---

## ⚙️ Setup & Run

1. **Clone**
   ```bash
   git clone https://github.com/your-username/TaskManager.git
   cd TaskManager
2. Open in Android Studio → let Gradle sync.

3. Run on an emulator or a physical device (minSdk as per your project).

---

##▶️ Usage

Tap ＋ to add a task (title, optional description/date).

Swipe or tap to mark complete.

Long-press or open details to edit/delete.


