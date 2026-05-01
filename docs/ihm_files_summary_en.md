# Mr. Jack Pocket - IHM (UI) Files Summary

This document summarizes the functions of the classes in the `IHM` (Interface Homme-Machine) package and the important methods they contain.

---

## 1. FenetrePrincipale.java
The main window (JFrame) of the application that serves as the container for all screens and manages navigation between them.
- **`FenetrePrincipale()`**: Initializes the main window properties, starts the background music, and sets up a `CardLayout` to hold and switch between different panels.
- **`afficherMenu()`, `afficherChoixMode()`, `afficherRegles()`**: Methods used to switch the currently visible view (Menu, Mode Selection, Rules) using the CardLayout.
- **`lancerJeu()`**: Receives the selected game mode and prints it (placeholder for starting the actual game session).
- **`main()`**: The entry point of the graphical application that launches the main window on the Swing event dispatch thread.

## 2. MenuPrincipalPanel.java
Represents the main menu interface, providing options to play, view rules, or quit the application.
- **`MenuPrincipalPanel()`**: Constructor that configures the layout, loads the background image, and creates the main navigation buttons.
- **`BoutonTexte()`**: A helper method that customizes the appearance of the buttons (transparent background, specific font, hand cursor).
- **`paintComponent()`**: Overridden to draw the loaded background image across the entire panel.

## 3. ChoixModePanel.java
The user interface panel responsible for selecting the desired game mode before starting a match.
- **`ChoixModePanel()`**: Sets up the UI layout with buttons for different game modes (Human vs Human, Human vs AI, AI vs AI) and a return button.
- **`BoutonTexte()`**: A helper method to style the buttons uniformly.
- **`paintComponent()`**: Overridden to draw the background image.

## 4. ReglesPanel.java
Manages the rules screen, allowing users to read the game instructions by navigating through image pages.
- **`ReglesPanel()`**: Sets up the UI with a scrollable image viewer and navigation buttons (Previous, Next, Return).
- **`afficherPage()`**: Dynamically loads the image corresponding to the current rule page, resizes it, and updates the page counter label.

## 5. MainMusique.java
Handles the playback of the continuous background music throughout the application.
- **`jouerMusique()`**: Opens the specified audio file, reduces its master volume significantly, and plays it in a continuous loop.
- **`arreterMusique()`**: Stops and closes the background audio clip if it is playing.

## 6. BoutonClickMusique.java
Manages the short sound effect triggered whenever a user clicks a button.
- **`jouerClick()`**: Loads and plays a brief click sound effect. It uses a timer to stop and close the audio clip shortly after it starts to free up resources.

## 7. GameMode.java
A simple enum class defining the available game modes.
- Contains the values: `HUMAN_VS_HUMAN`, `HUMAN_VS_IA`, and `IA_VS_IA`.
