# QR Scanner

A modern Android QR code scanner and generator app built with Jetpack Compose.

## Features

### 🔍 QR Code Scanning
- Real-time QR code scanning using camera
- Automatic detection and processing
- Support for various QR code formats
- Scan history with timestamps

### 🎨 QR Code Generation
- Generate QR codes from text input
- Customizable colors and styles
- Template selection
- Logo integration support
- Save and share generated QR codes

### 📱 User Interface
- Modern Material Design 3 UI
- Dark/Light theme support
- Intuitive navigation
- Smooth animations and transitions

### 📊 History Management
- View scan history
- Detailed view for each scanned QR code
- Delete individual items
- Clear all history option

### 🚀 Additional Features
- Batch scanning (coming soon)
- No ads - completely free
- Offline functionality
- Share QR codes easily

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with ViewModels
- **Database**: Room (SQLite)
- **Navigation**: Navigation Compose
- **Camera**: CameraX
- **QR Processing**: ZXing library
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)

## Project Structure

```
app/src/main/java/com/kottland/qrcanner/
├── data/
│   ├── database/
│   │   ├── AppDatabase.kt
│   │   ├── ScanResult.kt
│   │   └── ScanResultDao.kt
│   └── viewmodel/
│       ├── GeneratorViewModel.kt
│       └── HistoryViewModel.kt
├── ui/theme/
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
├── view/
│   ├── AboutUsScreen.kt
│   ├── BatchScanScreen.kt
│   ├── GeneratorScreen.kt
│   ├── HistoryDetailScreen.kt
│   ├── HistoryScreen.kt
│   ├── HomeScreen.kt
│   ├── OnboardingScreen.kt
│   ├── ScannerScreen.kt
│   ├── ScanResultScreen.kt
│   └── SettingsScreen.kt
└── MainActivity.kt
```

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 11 or later
- Android SDK with API 34

### Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/QRcanner.git
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Build and run the app:
```bash
./gradlew assembleDebug
```

### Permissions

The app requires the following permissions:
- `CAMERA` - For QR code scanning
- `WRITE_EXTERNAL_STORAGE` - For saving generated QR codes (API < 29)

## Usage

### Scanning QR Codes
1. Open the app and tap "Scan QR"
2. Point your camera at a QR code
3. The app will automatically detect and process the code
4. View the result and optionally save to history

### Generating QR Codes
1. Navigate to the "Generate QR" section
2. Enter your text or data
3. Customize colors and style (optional)
4. Generate and save/share your QR code

### Managing History
1. Access "History" from the main menu
2. View all previously scanned QR codes
3. Tap any item for detailed view
4. Delete individual items or clear all history

## Dependencies

Key dependencies used in this project:

- **Jetpack Compose**: Modern UI toolkit
- **Room**: Local database
- **Navigation Compose**: Navigation between screens
- **CameraX**: Camera functionality
- **ZXing**: QR code processing
- **ViewModel**: Architecture component
- **LiveData**: Reactive data

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- ZXing library for QR code processing
- Material Design team for design guidelines
- Android Jetpack team for modern development tools

## Support

If you encounter any issues or have questions, please:
1. Check the existing issues on GitHub
2. Create a new issue with detailed information
3. Provide steps to reproduce any bugs

---

**Enjoy scanning and generating QR codes with our modern, ad-free app!** 📱✨