# Home Workout

Home Workout is a modern Android fitness application built with Kotlin and Jetpack Compose. It is designed to help users stay consistent with home-based bodyweight workouts, track progress, and manage simple wellness routines without needing a gym or internet connection.

The app includes workout routines, custom workout creation, exercise guidance, progress tracking, and a lightweight meal/report dashboard. It is structured as a local-first Android app with Room persistence and Compose-based UI.

## Features

- Bodyweight workout plans
- Exercise playback and timer flow
- Countdown, work/rest phases, pause/resume controls
- Custom workout creation
- Workout detail screens and exercise info dialogs
- Progress tracking and workout completion history
- Offline meal plan data
- Water intake tracker
- Sound and voice feedback during workouts
- Settings screen for workout preferences
- Tab-based app navigation: Training, Meals, Report, Settings

## Tech Stack

- Kotlin
- Jetpack Compose
- Android Activity + Lifecycle
- Room Database
- ViewModel + StateFlow
- Navigation Compose
- Coroutines
- Retrofit + Moshi + OkHttp
- Firebase AI / Firebase App Check
- Gradle Kotlin DSL

## Project Structure

```text
my-home-workout/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   ├── model/
│   │   │   │   │   └── repository/
│   │   │   │   └── ui/
│   │   │   │       ├── components/
│   │   │   │       ├── screens/
│   │   │   │       ├── theme/
│   │   │   │       └── WorkoutViewModel.kt
│   │   │   └── res/
│   │   ├── androidTest/
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradle/
│   ├── wrapper/
│   └── libs.versions.toml
├── .env.example
├── metadata.json
├── .gitignore
└── gradle.properties
```

## App Architecture

This project follows a simple layered architecture:

- UI layer: Jetpack Compose screens
- State layer: WorkoutViewModel
- Data layer: Room DAO + repository
- Domain model: workout entities, user progress entities, custom workout entities

The application stores workout-related data locally via Room and exposes state through Flow-based ViewModel properties.

## Prerequisites

Before running this project, make sure you have:

- Android Studio
- JDK 17 or newer
- Android SDK configured
- Gradle wrapper available
- A Firebase project configured if you plan to enable Firebase features

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Let Gradle sync
4. Configure environment variables if required
5. Run the app on an emulator or physical Android device

## Environment Configuration

The project uses a .env-based setup pattern through the Secrets Gradle Plugin. The repository includes:

- .env.example

You may copy this and create your own .env file depending on the Firebase or environment configuration you plan to use.

## Build and Run

```bash
./gradlew assembleDebug
```

or run directly from Android Studio using the default app configuration.

## Notes

- The app is designed as an offline-first fitness tool.
- Firebase-related dependencies exist in the project, but some Firebase features may be optional depending on how the app is configured.
- The project currently appears to be a working foundation for a home workout app with a rich Compose UI and local persistence model.

## License

This project does not currently show a clear license file in the repository. If you plan to distribute or publish the project, add an appropriate open-source license.

## Contributing

Contributions are welcome. If you’d like to improve the app, consider:
- adding more exercise libraries
- improving workout analytics
- refining meal tracking
- expanding custom routine logic
- improving onboarding and accessibility
