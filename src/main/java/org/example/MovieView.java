package org.example;

import java.util.List;
import java.util.Collection;
import com.googlecode.lanterna.TextColor;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalFactory;
import com.googlecode.lanterna.graphics.*;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;


import com.googlecode.lanterna.*;

public class MovieView {
    private final Terminal terminal;
    private final Screen screen;

    private final MultiWindowTextGUI textGUI;
    private final MovieController movieController;

    public MovieView(MovieController movieController) throws Exception {
        this.movieController = movieController;
        // Create the terminal
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        this.terminal = terminalFactory.createTerminal();

        // Create the TerminalScreen from the terminal
        this.screen = new TerminalScreen(terminal); // Create the TerminalScreen instance
        this.screen.startScreen(); // Start the screen

        // Create the MultiWindowTextGUI with the screen
        this.textGUI = new MultiWindowTextGUI(screen);
    }

    public void run() {
        showMainMenu();
    }

    private void showMainMenu() {
        Panel mainMenuPanel = new Panel();
        mainMenuPanel.addComponent(new Label("Wybierz bazę filmową:"));

        Button tmdbButton = new Button("TMDb", this::showTmdbMenu);
        Button exitButton = new Button("Zakończ", this::exitApplication);

        mainMenuPanel.addComponent(tmdbButton);
        mainMenuPanel.addComponent(exitButton);

        // Tworzenie nowego BasicWindow z odpowiednim tytułem i panelem
        BasicWindow mainMenuWindow = new BasicWindow("Menu główne");
        mainMenuWindow.setComponent(mainMenuPanel); // Ustawienie komponentu panelu na oknie

        textGUI.addWindowAndWait(mainMenuWindow); // Dodanie okna i oczekiwanie na zamknięcie
    }

    private void showTmdbMenu() {
        Panel tmdbPanel = new Panel();
        tmdbPanel.addComponent(new Label("Menu TMDb:"));

        Button searchMovieButton = new Button("Wyszukaj film", this::searchMovie);
        Button nowPlayingButton = new Button("Obecnie grane filmy", this::displayNowPlaying);
        Button topRatedButton = new Button("Najlepiej oceniane filmy", this::displayTopRated);
        Button favoritesButton = new Button("Ulubione filmy", this::displayFavoriteMovies);
        Button backButton = new Button("Powrót", this::showMainMenu);

        tmdbPanel.addComponent(searchMovieButton);
        tmdbPanel.addComponent(nowPlayingButton);
        tmdbPanel.addComponent(topRatedButton);
        tmdbPanel.addComponent(favoritesButton);
        tmdbPanel.addComponent(backButton);

        // Tworzenie nowego BasicWindow
        BasicWindow tmdbWindow = new BasicWindow("Menu TMDb");
        tmdbWindow.setComponent(tmdbPanel); // Ustawienie komponentu panelu na oknie

        textGUI.addWindowAndWait(tmdbWindow); // Dodanie okna i oczekiwanie na zamknięcie
    }

    private void searchMovie() {
        // Implementacja wyszukiwania filmu
        String title = promptForInput("Podaj tytuł filmu:");
        Movie movie = movieController.fetchMovieDetails(title);
        if (movie != null) {
            displayMovieDetails(movie);
        } else {
            displayMessage("Film nie został znaleziony.");
        }
    }

    private void displayNowPlaying() {
        // Implementacja wyświetlania obecnie granych filmów
        List<Movie> nowPlayingMovies = movieController.fetchNowPlayingMovies();
        if (nowPlayingMovies.isEmpty()) {
            displayMessage("Brak obecnie granych filmów.");
        } else {
            for (Movie movie : nowPlayingMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    private void displayTopRated() {
        // Implementacja wyświetlania najlepiej ocenianych filmów
        List<Movie> topRatedMovies = movieController.fetchTopRatedMovies();
        if (topRatedMovies.isEmpty()) {
            displayMessage("Brak najlepiej ocenianych filmów.");
        } else {
            for (Movie movie : topRatedMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    private void displayFavoriteMovies() {
        // Implementacja wyświetlania ulubionych filmów
        List<Movie> favoriteMovies = movieController.getFavoriteMovies();
        if (favoriteMovies.isEmpty()) {
            displayMessage("Brak ulubionych filmów.");
        } else {
            for (Movie movie : favoriteMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    private void displayMovieDetails(Movie movie) {
        String message = String.format("Tytuł: %s\nReżyser: %s\nData produkcji: %s\nOpis: %s\nOcena: %s\n",
                movie.getTitle(), movie.getDirector(), movie.getReleaseDate(), movie.getOverview(), movie.getRating());
        displayMessage(message);
    }

    public void displayMessage(String message) {
        Panel messagePanel = new Panel();
        messagePanel.addComponent(new Label(message));

        Button okButton = new Button("OK", () -> {
            // Close the last BasicWindow
            Collection<Window> windows = textGUI.getWindows();
            BasicWindow lastWindow = null;

            // Find the last BasicWindow in the collection
            for (Window window : windows) {
                if (window instanceof BasicWindow) {
                    lastWindow = (BasicWindow) window; // Cast to BasicWindow
                }
            }

            // Remove the last BasicWindow if found
            if (lastWindow != null) {
                textGUI.removeWindow(lastWindow);
            }
        });

        messagePanel.addComponent(okButton);

        BasicWindow messageWindow = new BasicWindow("Informacja");
        messageWindow.setComponent(messagePanel);

        textGUI.addWindowAndWait(messageWindow);
    }

    private String promptForInput(String prompt) {
        Panel inputPanel = new Panel();
        inputPanel.addComponent(new Label(prompt));

        TextBox inputBox = new TextBox();
        inputPanel.addComponent(inputBox);

        Button okButton = new Button("OK", () -> {
            // Close the last BasicWindow
            Collection<Window> windows = textGUI.getWindows();
            BasicWindow lastWindow = null;

            // Find the last BasicWindow in the collection
            for (Window window : windows) {
                if (window instanceof BasicWindow) {
                    lastWindow = (BasicWindow) window; // Cast to BasicWindow
                }
            }

            // Remove the last BasicWindow if found
            if (lastWindow != null) {
                textGUI.removeWindow(lastWindow);
            }
        });

        inputPanel.addComponent(okButton);

        BasicWindow inputWindow = new BasicWindow("Input");
        inputWindow.setComponent(inputPanel);

        textGUI.addWindowAndWait(inputWindow);

        return inputBox.getText();
    }

    public String getMovieTitle() {
        String title = promptForInput("Podaj tytuł filmu:");
        return title;
    }

    private void exitApplication() {
        System.exit(0);
    }
}
