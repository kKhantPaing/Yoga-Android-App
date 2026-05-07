# Yoga-Android-App

Yoga-Android-App is a yoga class management platform with two complementary applications:

- **Flutter**: User-facing client app for discovering, registering, and enrolling in yoga classes.
- **Native Android**: Admin panel for managing classes, users, registrations, and enrollments.

Both apps are designed to work together and support backend-driven workflows for yoga studios, instructors, and students.

## Project Structure

```
Yoga-Android-App/
├── Flutter/                          # Flutter client app
│   ├── app/                          # Flutter application module
│   ├── build.gradle.kts              # Build configuration
│   ├── settings.gradle.kts           # Gradle settings
│   ├── gradle/                       # Gradle wrapper and dependency configuration
│   └── local.properties              # Local environment settings
└── Native/                           # Native Android admin app
    ├── app/                          # Android application module
    │   ├── src/
    │   │   ├── main/                 # Main source code
    │   │   ├── androidTest/          # Instrumentation tests
    │   │   └── test/                 # Local unit tests
    │   └── build.gradle              # Module build file
    ├── build.gradle                  # Root build configuration
    ├── settings.gradle               # Gradle settings
    ├── gradle/                       # Gradle wrapper files
    └── local.properties              # Local environment settings
```

## Features

### Flutter (Client)
- Browse yoga classes with detailed descriptions
- User registration and authentication
- Class enrollment and booking
- Manage current enrollments

### Native Android (Admin)
- Create and manage yoga classes
- Monitor and approve class registrations
- Track enrollment status
- Connect to backend APIs for live data updates

## Requirements

### Flutter App
- Flutter SDK 3.0+
- Dart 2.17+
- Android SDK API level 21+
- Android Studio or compatible editor
- Gradle 7+

### Native Android App
- Android SDK API level 21+
- Android Studio
- Java 11+
- Gradle 7+

## Setup

### Flutter App
1. Install Flutter: https://flutter.dev/docs/get-started/install
2. Open a terminal and navigate to the Flutter folder:
   ```bash
   cd Flutter
   ```
3. Install dependencies:
   ```bash
   flutter pub get
   ```
4. Start a device or emulator:
   ```bash
   flutter devices
   ```
5. Run the app:
   ```bash
   flutter run
   ```

### Native Android App
1. Install Android Studio: https://developer.android.com/studio
2. Open the `Native` folder in Android Studio.
3. Configure `local.properties` if your SDK path is not detected:
   ```properties
   sdk.dir=C:\Users\<YourUser>\AppData\Local\Android\sdk
   ```
4. Sync Gradle and build the project.

## Build & Run

### Flutter App
**Debug:**
```bash
cd Flutter
flutter run
```

**Release:**
```bash
flutter build apk --release
flutter build appbundle --release
```

**Run on device:**
```bash
flutter run -d <device_id>
```

### Native Android App
**Debug via Android Studio:**
- Use the Run action or press `Shift + F10`

**Debug via Gradle:**
```bash
cd Native
./gradlew assembleDebug
./gradlew installDebug
```

**Release:**
```bash
./gradlew assembleRelease
./gradlew bundleRelease
```

**Tests:**
```bash
./gradlew test
./gradlew connectedAndroidTest
```

## Notes

- The Flutter app is intended for yoga students and clients.
- The Flutter client requires backend API integration to function; without API support it cannot be used.
- The Native Android app is intended for studio administrators and instructors.
- Backend API configuration and authentication setup may require additional environment-specific settings.
- The `Flutter/` project is the **client-side** application for end users.
- The `Native/` project is the **admin-side** application for administrators.
- Both implementations share the same backend API (to be integrated).
- Native Android is optimized specifically for admin dashboard functionality.
- Use the appropriate folder based on whether you're developing user or admin features.

