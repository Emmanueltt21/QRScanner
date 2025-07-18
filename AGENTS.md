# QR Scanner & Generator App

This file contains important information about the project.

## Commands

- Build the app: `./gradlew build`
- Run tests: `./gradlew test`
- Install the app: `./gradlew installDebug`

## Project Structure

The project follows the MVVM (Model-View-ViewModel) architecture.

- `data`: Contains the data layer, including the Room database and repositories.
- `model`: Contains the data models for the application.
- `ui`: Contains the UI layer, including Jetpack Compose screens and themes.
- `view`: Contains the views (Composables).
- `viewmodel`: Contains the ViewModels.

## Dependencies

- **ML Kit Barcode Scanning:** For scanning QR codes and barcodes.
- **ZXing:** For generating QR codes.
- **Room:** For storing the scan history.
- **Jetpack Compose:** For building the UI.
- **Jetpack Navigation:** For navigating between screens.
