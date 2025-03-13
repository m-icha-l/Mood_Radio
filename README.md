# Mood_Radio

**Mood_Radio** to aplikacja mobilna, która pozwala użytkownikom słuchać radia online w zależności od ich nastroju. Aplikacja oferuje wybór ulubionych stacji radiowych oraz możliwość dodawania własnych stacji, umożliwiając spersonalizowanie doświadczenia słuchania muzyki. Dzięki integracji z ExoPlayer, aplikacja zapewnia płynne odtwarzanie stacji radiowych bez opóźnień.

## Funkcje

- **Wybór stacji radiowych**: Wybieraj z listy dostępnych stacji radiowych i słuchaj na żywo.
- **Dodawanie własnych stacji**: Użytkownicy mogą dodać własne stacje radiowe przez podanie linku URL i nazwy.
- **Zarządzanie stacjami**: Przechowywanie ulubionych stacji radiowych w aplikacji.
- **Pauzowanie/odtwarzanie**: Umożliwia pauzowanie i kontynuowanie odtwarzania radia.

## Instalacja

Aby zainstalować i uruchomić aplikację, wykonaj poniższe kroki:

1. **Pobierz projekt**
   - Możesz pobrać projekt z GitHub, klonując repozytorium:
     ```bash
     git clone https://github.com/twoj-repozytorium/Mood_Radio.git
     ```

2. **Zainstaluj Android Studio**
   - Upewnij się, że masz zainstalowane Android Studio na swoim komputerze. Jeśli nie, pobierz je [tutaj](https://developer.android.com/studio).

3. **Otwórz projekt**
   - Otwórz Android Studio i wybierz opcję **Open an existing Android Studio project**. Wybierz folder, w którym sklonowałeś projekt.

4. **Zainstaluj zależności**
   - Android Studio automatycznie zainstaluje wszystkie wymagane zależności, gdy otworzysz projekt po raz pierwszy. Upewnij się, że masz połączenie z internetem, aby pobrać wszystkie paczki.

5. **Uruchom aplikację**
   - Podłącz urządzenie Android lub uruchom emulator. Naciśnij **Run** w Android Studio, aby uruchomić aplikację.

## Technologie

- **Kotlin** – Język programowania użyty do napisania aplikacji.
- **Jetpack Compose** – Framework UI do tworzenia nowoczesnych interfejsów użytkownika.
- **ExoPlayer** – Framework do odtwarzania mediów, używany do odtwarzania radia online.
- **SharedPreferences** – Używane do przechowywania danych użytkownika, takich jak ulubione stacje radiowe.
- **JSON** – Format przechowywania danych o stacjach radiowych.

## Struktura projektu

- **`MainActivity.kt`**: Główna aktywność aplikacji. Zarządza nawigacją i UI.
- **`Model_logic.kt`**: Logika aplikacji, która obsługuje dane o stacjach radiowych oraz odtwarzanie radia.
- **`RadioPlayerScreen.kt`**: Ekran, na którym użytkownicy mogą wybierać stacje radiowe do odtwarzania.
- **`First_Screen.kt`**: Ekran umożliwiający dodanie nowych stacji radiowych przez użytkownika.

## Funkcjonalności

1. **RadioPlayerScreen**
   - Użytkownicy mogą wybierać stacje z listy lub dodać własne.
   - Przycisk Play/Pause umożliwia odtwarzanie lub zatrzymywanie radia.

2. **First_Screen**
   - Umożliwia dodanie własnych stacji radiowych poprzez wprowadzenie URL i nazwy stacji.
