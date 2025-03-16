# Mood_Radio

**Mood_Radio** is a mobile app that allows users to listen to online radio stations depending on their mood. The app offers a selection of favorite radio stations and the ability to add custom stations, enabling a personalized music listening experience. With integration with ExoPlayer, the app ensures smooth playback of radio stations without delays.

## Features

- **Radio Station Selection**: Choose from a list of available radio stations and listen live.
- **Add Custom Stations**: Users can add their own radio stations by providing a URL and name.
- **Manage Stations**: Store your favorite radio stations within the app.
- **Pause/Play**: Allows pausing and resuming radio playback.

## Installation

To install and run the app, follow these steps:

1. **Download the Project**
   - You can download the project from GitHub by cloning the repository:
     ```bash
     git clone https://github.com/your-repository/Mood_Radio.git
     ```

2. **Install Android Studio**
   - Make sure you have Android Studio installed on your computer. If not, download it [here](https://developer.android.com/studio).

3. **Open the Project**
   - Open Android Studio and select the **Open an existing Android Studio project** option. Choose the folder where you cloned the project.

4. **Install Dependencies**
   - Android Studio will automatically install all required dependencies when you open the project for the first time. Make sure you're connected to the internet to download all packages.

5. **Run the App**
   - Connect an Android device or run an emulator. Press **Run** in Android Studio to launch the app.

## Technologies

- **Kotlin** – Programming language used to write the app.
- **Jetpack Compose** – UI framework for creating modern user interfaces.
- **ExoPlayer** – Media playback framework used to stream online radio.
- **SharedPreferences** – Used to store user data, such as favorite radio stations.
- **JSON** – Format for storing data about radio stations.

## Project Structure

- **`MainActivity.kt`**: The main activity of the app. Manages navigation and the UI.
- **`Model_logic.kt`**: The app's logic that handles radio station data and playback.
- **`RadioPlayerScreen.kt`**: The screen where users can choose radio stations to play.
- **`First_Screen.kt`**: The screen that allows users to add new radio stations.

## Functionality

1. **RadioPlayerScreen**
   - Users can choose stations from the list or add their own.
   - The Play/Pause button allows users to play or stop the radio.

2. **First_Screen**
   - Allows users to add their own radio stations by entering the URL and name of the station.
