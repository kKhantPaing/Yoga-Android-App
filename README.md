# Yoga-Android-App

A comprehensive Yoga class management system with dual implementations: Flutter for client-side user applications and Native Android for admin-side management.

## Overview

Yoga-Android-App is a complete yoga class management platform with two separate applications:
- **Flutter (Client Side)**: User-facing application for discovering, registering, and enrolling in yoga classes
- **Native Android (Admin Side)**: Administrative dashboard for managing classes, users, and enrollments

Both applications work in tandem to provide a complete yoga class management ecosystem with user authentication, class management, registration handling, and enrollment tracking.

## Features

### Client Side (Flutter)
- **Browse Yoga Classes**: Discover available classes with detailed information
- **User Registration & Authentication**: Secure account creation and login
- **Class Registration**: Register for available yoga classes
- **Enrollment Management**: View and manage active enrollments
- **Real-time Updates**: Live class availability and status notifications
- **Cross-Platform Support**: Available on iOS, Android, and Web

### Admin Side (Native Android)
- **Class Management**: Create, edit, and manage yoga classes
- **User Management**: Manage user accounts and permissions
- **Registration Management**: Monitor and approve class registrations
- **Enrollment Tracking**: Track and manage student enrollments
- **Analytics Dashboard**: View enrollment statistics and class performance
- **Backend Integration**: API-driven administration tools

## Project Structure

```
Yoga-Android-App/
├── Flutter/                          # Client-side application (Cross-platform)
│   ├── app/                         # Flutter application module
│   ├── build.gradle.kts             # Flutter build configuration
│   ├── settings.gradle.kts          # Flutter settings configuration
│   ├── gradle/                      # Gradle wrapper and dependencies
│   └── local.properties             # Local environment configuration
│
└── Native/                          # Admin-side application (Native Android)
    ├── app/                         # Android application module
    │   ├── src/
    │   │   ├── main/               # Main application source code
    │   │   ├── androidTest/        # Android instrumentation tests
    │   │   └── test/               # Unit tests
    │   └── build.gradle            # Android build configuration
    ├── build.gradle                # Root build configuration
    ├── settings.gradle             # Project settings
    ├── gradle/                     # Gradle wrapper and utilities
    └── local.properties            # Local environment configuration
```

## Requirements & Prerequisites

### Flutter App
- **Flutter SDK**: Version 3.0 or higher
- **Dart**: Version 2.17 or higher
- **Android SDK**: API level 21 (Android 5.0) or higher
- **Android Studio**: Latest stable version (optional but recommended)
- **Gradle**: 7.0 or higher

### Native Android App
- **Android SDK**: API level 21 (Android 5.0) or higher
- **Minimum API Level**: 21
- **Target API Level**: 34 or higher
- **Android Studio**: Latest stable version
- **Gradle**: 7.0 or higher
- **Java**: Version 11 or higher

## Installation & Setup

### Flutter App Setup (Client Side)

1. **Install Flutter SDK**:
   - Visit https://flutter.dev/docs/get-started/install
   - Follow platform-specific installation instructions

2. **Clone and navigate to the Flutter project**:
   ```bash
   cd Flutter
   ```

3. **Install dependencies**:
   ```bash
   flutter pub get
   ```

4. **Connect a device or start an emulator**:
   ```bash
   flutter devices
   ```

5. **Run the application**:
   ```bash
   flutter run
   ```

### Native Android App Setup (Admin Side)

1. **Install Android Studio and Android SDK**:
   - Download from https://developer.android.com/studio
   - Install required SDK versions and tools

2. **Clone and open the Native project**:
   ```bash
   cd Native
   ```

3. **Open in Android Studio**:
   - File → Open → Select the `Native` folder
   - Android Studio will automatically sync Gradle files

4. **Configure local.properties** (if needed):
   ```properties
   sdk.dir=/path/to/your/android/sdk
   ```

## Build & Run Instructions

### Flutter App (Client Side)

**Debug Build**:
```bash
cd Flutter
flutter run
```

**Release Build**:
```bash
flutter build apk --release
flutter build appbundle --release
```

**Run on specific device**:
```bash
flutter run -d <device_id>
```

### Native Android App (Admin Side)

**Debug Build** (via Android Studio):
- Click the "Run" button or press `Shift + F10`

**Debug Build** (via Gradle):
```bash
cd Native
./gradlew assembleDebug
./gradlew installDebug
```

**Release Build**:
```bash
./gradlew assembleRelease
./gradlew bundleRelease
```

**Run tests**:
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest   # Instrumentation tests
```

## Architecture

### Flutter Architecture (Client Side)
- **Framework**: Flutter with Dart
- **Platform Target**: iOS, Android, and Web
- **State Management**: (To be configured)
- **API Communication**: HTTP client for backend integration
- **Navigation**: Flutter routing system
- **Data Persistence**: Local storage for user preferences and enrolled classes
- **Purpose**: User-facing application for class discovery and enrollment

### Native Android Architecture (Admin Side)
- **Language**: Java
- **Platform Target**: Android only
- **Build System**: Gradle
- **API Communication**: Retrofit/OkHttp for backend integration
- **Architecture Pattern**: (To be configured)
- **Data Persistence**: SharedPreferences and SQLite
- **Purpose**: Administrative dashboard for class and enrollment management

## Development

### Flutter Development
```bash
cd Flutter
flutter pub get
flutter run --debug
```

### Native Development
```bash
cd Native
./gradlew build
```

Build outputs will be available in respective `build/` directories.

## Notes

- The `Flutter/` project is the **client-side** application for end users
- The `Native/` project is the **admin-side** application for administrators
- Both implementations share the same backend API (to be integrated)
- Flutter provides cross-platform support (iOS, Android, Web) for users
- Native Android is optimized specifically for admin dashboard functionality
- Use the appropriate folder based on whether you're developing user or admin features
- API integration details will be added in future updates

