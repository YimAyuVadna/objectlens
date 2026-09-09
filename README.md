# ObjectLens

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20%7C%20Material%203-blue.svg)](https://developer.android.com/jetpack/compose)

**ObjectLens** is a smart, real-time object recognition and visual learning assistant for Android. Built with modern Android development practices—including Jetpack Compose, CameraX, and Room Database—ObjectLens detects objects in your environment, provides in-depth encyclopedic context, speaks descriptions aloud, and allows users to teach the system new objects.

---

## ✨ Features

- **Real-Time Detection & Bounding Boxes**: Smooth, live camera feed with dynamic bounding box visualization and confidence metrics.
- **Encyclopedic Knowledge Base**: Detailed information including physical characteristics, origins, practical uses, and curated facts for recognized items.
- **Teach Object Capability**: Interactive training mode allowing you to label, describe, and register new custom objects directly from your camera.
- **Voice Narration (Text-to-Speech)**: Integrated audio readout delivering hands-free accessibility and pronunciation.
- **Tactile Haptic Feedback**: Subtle vibration cues on scan events and detection confirmations.
- **Offline History & Bookmarks**: Full offline-first storage backed by Room Database to preserve your scan history and favorite items.
- **Modern Jetpack Compose UI**: Clean Material 3 design system supporting responsive layouts, smooth animations, and dynamic theming.

---

## 🛠️ Tech Stack & Architecture

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/)
- **Architecture**: MVVM (Model-View-ViewModel) pattern with unidirectional data flow
- **Camera**: [CameraX](https://developer.android.com/training/camerax) (Lifecycle, View, and Preview analysis)
- **Local Persistence**: [Room Database](https://developer.android.com/training/data-storage/room) & DataStore Preferences
- **Concurrency**: Kotlin Coroutines & `StateFlow`
- **Audio & Sensory**: Android `TextToSpeech` engine and `Vibrator` / `VibratorManager`

---

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Koala / Ladybug or newer recommended)
- Android SDK 35
- JDK 17+
- Android device or emulator running Android 8.0 (API level 26) or higher with camera support

### Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/YimAyuVadna/objectlens.git
   cd objectlens
   ```

2. **Open the project in Android Studio**:
   - Launch Android Studio.
   - Select **Open** and select the `objectlens` directory.
   - Allow Gradle to sync and download dependencies.

3. **Configure Environment (Optional)**:
   - If utilizing optional cloud API extensions, copy `.env.example` to `.env`:
     ```bash
     cp .env.example .env
     ```
   - Add your API credentials in `.env` as needed.

4. **Build & Run**:
   - Select your connected Android device or emulator from the device menu.
   - Click the **Run** button (`Shift + F10` or ▶).

---

## 📁 Project Structure

```text
app/src/main/java/com/example/
├── data/
│   ├── local/          # Room Database, DAOs, Entities, and Knowledge Base
│   ├── repository/     # Repository pattern implementation
│   └── sync/           # Local and cloud synchronization logic
├── ml/                 # Object detection engine, label memory & feature extractors
├── ui/
│   ├── components/     # Reusable Compose components (Overlays, BottomSheets, Dialogs)
│   ├── navigation/     # Jetpack Compose Navigation routes
│   ├── screens/        # Screen composables (Scanner, History, Favorites, Settings, etc.)
│   ├── theme/          # Theme, color palettes, and typography
│   └── viewmodel/      # ViewModel classes managing UI state
└── util/               # Helper managers (TTS, Haptics, Preferences)
```

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.
