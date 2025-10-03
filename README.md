# TireWise
TireWise is a comprehensive Android application designed to help users monitor and manage the health of their vehicle's tires. By leveraging on-device machine learning, users can instantly assess tire conditions, track scan history, and set maintenance reminders, ensuring vehicle safety and longevity.

# Link to Video Pitch: [(https://youtu.be/tOAmqhrFSu4)]
# Link to App Demo: [(https://youtu.be/0GDE8-tsXek)]

## Features

*   **Tire Condition Analysis**: Scan your tires using your phone's camera to get an instant condition assessment (Good, Moderate, or Poor) powered by a local TensorFlow Lite model.
*   **Vehicle Management**: Easily add, edit, and manage multiple vehicles. Store details such as license plate, make, model, and custom names.
*   **Scan History**: Maintain a detailed log of all tire scans for each vehicle. Scans are organized by position (e.g., Front Left, Back Right) and can be sorted by date or condition.
*   **Maintenance Reminders**: Set customizable notifications for important vehicle maintenance tasks, including tire checkups, rotations, alignments, oil changes, and insurance renewals.
*   **Google Authentication**: Securely sign in with your Google account to sync your vehicle data and scan history.
*   **Tire Catalog**: Browse an informative catalog of different tire types and their suitability for various terrains like highways, snow, and off-road conditions.
*   **Offline Functionality**: The app checks for network connectivity and allows core features like tire scanning to be used even without an internet connection.

## How It Works

1.  **Authentication**: Users can sign in with their Google account to access all features or proceed as a guest for basic tire scanning.
2.  **Vehicle Registration**: Add one or more vehicles to your personal garage within the app.
3.  **Tire Scanning**: Select a vehicle and use the in-app camera to take a picture of a tire.
4.  **Instant Analysis**: The on-device machine learning model analyzes the image and provides an immediate assessment of the tire's condition.
5.  **Save & Log**: For registered users, the scan result, along with any notes, can be saved to the vehicle's history, specifying the tire's position.
6.  **Review History**: Access the history for any vehicle to see past scans, sorted and filtered as needed.
7.  **Set Reminders**: Create push notifications for upcoming maintenance to ensure your vehicle stays in top shape.

## Tech Stack

*   **Language**: [Kotlin](https://kotlinlang.org/)
*   **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
*   **Architecture**: MVVM (Model-View-ViewModel)
*   **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
*   **Machine Learning**: [TensorFlow Lite](https://www.tensorflow.org/lite) for on-device inference.
*   **Backend & Database**: [Firebase](https://firebase.google.com/) (Authentication, Firestore, Cloud Storage)
*   **Navigation**: [Jetpack Navigation for Compose](https://developer.android.com/jetpack/compose/navigation)
*   **Camera**: [CameraX](https://developer.android.com/training/camerax)
*   **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
*   **Animations**: [Lottie](https://airbnb.io/lottie/) for splash screen animations.

## Project Structure

The project follows a standard MVVM architecture, with a clear separation of concerns.

```
/app/src/main/
├── java/com/mrm/tirewise/
│   ├── authentication/   # Google Sign-In logic
│   ├── model/            # Data classes (Vehicle, TireScan, Reminder)
│   ├── navigation/       # Jetpack Compose navigation graph and destinations
│   ├── networkConnectivity/ # Observes and reports network status
│   ├── reminderManager/  # Handles scheduling and triggering of notifications
│   ├── repository/       # Data layer, communicates with Firebase
│   ├── utils/            # Helper functions for images, dates, and ML model
│   ├── view/             # Composable screens, reusable UI components, and theme
│   └── viewModel/        # Manages UI state and business logic
└── ml/
    ├── labels.txt        # Labels for the ML model (Bad, Moderate, Good)
    └── model.tflite      # The trained TensorFlow Lite model
```

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   Android Studio Iguana | 2023.2.1 or newer
*   JDK 17

### Installation

1.  Clone the repository:
    ```sh
    git clone https://github.com/ridhwan-ra2005171/TireWise.git
    ```
2.  Open the project in Android Studio.
3.  Let Gradle sync the dependencies. The required `google-services.json` file is included in the repository, so no additional Firebase setup is needed to build the project.
4.  Run the app on an Android emulator or a physical device.
