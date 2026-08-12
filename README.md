# 📝 TaskManager — Application Android de Gestion de Tâches

> Une application Android moderne pour créer, organiser et suivre vos tâches quotidiennes, bâtie sur une architecture MVVM propre et scalable.

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Kotlin-1.x-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
  <img src="https://img.shields.io/badge/Architecture-MVVM-blue" alt="MVVM">
  <img src="https://img.shields.io/badge/Build-Gradle%20Kotlin%20DSL-02303A?logo=gradle&logoColor=white" alt="Gradle">
  <img src="https://img.shields.io/badge/License-MIT-black.svg" alt="License: MIT">
</p>

---

## 📌 Aperçu du projet

**TaskManager** est une application Android native permettant aux utilisateurs de **créer, modifier et suivre leurs tâches quotidiennes** au sein d'une interface épurée et intuitive. Conçue avec **Kotlin** et les composants **Android Jetpack**, elle repose sur une architecture **MVVM (Model-View-ViewModel)** associée à un pattern **Repository**, garantissant scalabilité, testabilité et séparation claire des responsabilités.

Ce projet s'adresse aux **recruteurs et Tech Leads évaluant des compétences en développement Android natif**, ainsi qu'aux développeurs souhaitant une base solide pour construire des applications de productivité.

---

## ✨ Fonctionnalités clés

- ✅ **Créer, modifier et supprimer** des tâches
- 📋 **Liste consultable** de toutes les tâches en un coup d'œil
- 🕓 **Marquage** des tâches comme terminées ou en attente
- 🔔 Rappels & notifications *(fonctionnalité optionnelle/évolutive)*
- 💾 **Persistance locale** des données via Room (SQLite)
- 🎨 Interface soignée respectant les principes du **Material Design**

---

## 🏗️ Architecture & Stack Technique

L'application suit le pattern **MVVM + Repository**, standard recommandé par Google pour les applications Android modernes :

- **Model** : classes de données / entités Room
- **ViewModel** : détenteur d'état conscient du cycle de vie (lifecycle-aware) pour la UI
- **Repository** : source unique de vérité pour l'accès aux données
- **View (UI)** : Activities/Fragments + adaptateurs RecyclerView

| Composant | Technologie |
|---|---|
| **Langage** | Kotlin |
| **Architecture** | MVVM + Repository Pattern |
| **Base de données** | Room (SQLite) |
| **Jetpack** | ViewModel, LiveData / StateFlow, Room |
| **UI** | Layouts XML, Material Design |
| **Build system** | Gradle (Kotlin DSL) |
| **IDE** | Android Studio |

---

## 🚀 Guide d'installation & Démarrage rapide

### Prérequis
- [Android Studio](https://developer.android.com/studio) (dernière version stable recommandée)
- JDK 11+
- Un émulateur Android configuré ou un appareil physique

### 1️⃣ Cloner le projet
```bash
git clone https://github.com/ELGHAD/TaskManager-App.git
cd TaskManager-App
```

### 2️⃣ Ouvrir dans Android Studio
Ouvrez le dossier du projet dans Android Studio et laissez **Gradle synchroniser** automatiquement les dépendances.

### 3️⃣ Lancer l'application
Sélectionnez un émulateur ou connectez un appareil physique, puis cliquez sur **Run ▶️** (le `minSdk` requis est défini dans le fichier `build.gradle` du module `taskmanager`).

---

## 📁 Structure du projet

```
TaskManager-App/
├── taskmanager/            # Module principal de l'application Android
│   ├── src/                # Code source Kotlin (Model, ViewModel, Repository, UI)
│   └── build.gradle.kts    # Configuration Gradle du module
├── README.md
```

---

## ▶️ Utilisation

1. Appuyez sur **➕** pour ajouter une tâche (titre, description et date optionnels).
2. **Balayez** ou **appuyez** sur une tâche pour la marquer comme terminée.
3. **Appui long** ou ouverture des détails pour **modifier** ou **supprimer** une tâche.

---

## 🔒 Sécurité & Bonnes pratiques

- **Architecture MVVM** garantissant une séparation stricte entre logique métier et interface utilisateur.
- **Room (ORM)** pour un accès sécurisé et typé à la base de données SQLite locale, limitant les risques d'injection.
- **Repository Pattern** centralisant l'accès aux données et facilitant les tests unitaires (mocking).
- **ViewModel lifecycle-aware** évitant les fuites mémoire liées au cycle de vie des Activities/Fragments.
- Licence **MIT** favorisant la réutilisation et la contribution ouverte.

---

## 👤 Auteur & Contact

**ELGHAD**
- GitHub : [@ELGHAD](https://github.com/ELGHAD)
- Projet : [TaskManager-App](https://github.com/ELGHAD/TaskManager-App)

⭐ N'hésitez pas à *star* le dépôt ou à contribuer via une *pull request* !
