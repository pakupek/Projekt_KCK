package org.example;
import java.util.List;
import java.util.Collection;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class MovieView {
    private final Terminal terminal;
    private final Screen screen;

    private final MultiWindowTextGUI textGUI;
    private final MovieController movieController;

    public MovieView(MovieController movieController) throws Exception {
        this.movieController = movieController;
        // Tworzenie terminala
        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        this.terminal = terminalFactory.createTerminal();

        this.screen = new TerminalScreen(terminal); // Tworzenie instancji TerminalScreen
        this.screen.startScreen();

        // Tworzenie MultiWindowTextGUI
        this.textGUI = new MultiWindowTextGUI(screen);
    }

    public void run() {
        showMainMenu();
    }


    // Wybranie menu z jakiej bazy mamy korzystac
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

    // Menu dla bazy Tmdb
    private void showTmdbMenu() {
        Panel tmdbPanel = new Panel();
        tmdbPanel.addComponent(new Label("Menu TMDb:"));

        Button searchMovieButton = new Button("Wyszukaj film", this::displaySearchMovie);
        Button nowPlayingButton = new Button("Obecnie grane filmy", this::displayNowPlaying);
        Button topRatedButton = new Button("Najlepiej oceniane filmy", this::displayTopRated);
        Button favoritesButton = new Button("Ulubione filmy", this::displayFavoriteMovies);
        Button addFavorites = new Button("Dodaj do ulubionych",() -> movieController.addToFavorites());
        Button backButton = new Button("Powrót", this::showMainMenu);

        tmdbPanel.addComponent(searchMovieButton);
        tmdbPanel.addComponent(nowPlayingButton);
        tmdbPanel.addComponent(topRatedButton);
        tmdbPanel.addComponent(favoritesButton);
        tmdbPanel.addComponent(addFavorites);
        tmdbPanel.addComponent(backButton);

        // Tworzenie nowego BasicWindow
        BasicWindow tmdbWindow = new BasicWindow("Menu TMDb");
        tmdbWindow.setComponent(tmdbPanel); // Ustawienie komponentu panelu na oknie

        textGUI.addWindowAndWait(tmdbWindow); // Dodanie okna i oczekiwanie na zamknięcie
    }

    private void displaySearchMovie() {
        String title = promptForInput("Podaj tytuł filmu:");
        Movie movie = movieController.fetchMovieDetails(title);
        if (movie != null) {
            displayMovieDetails(movie);
        } else {
            displayMessage("Film nie został znaleziony.");
        }
    }

    // Wyswietlenie obecnie granych filmów
    private void displayNowPlaying() {
        List<Movie> nowPlayingMovies = movieController.fetchNowPlayingMovies();
        if (nowPlayingMovies.isEmpty()) {
            displayMessage("Brak obecnie granych filmów.");
        } else {
            for (Movie movie : nowPlayingMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    // Wyświetlenie najlepeij ocenianych filmów
    private void displayTopRated() {
        List<Movie> topRatedMovies = movieController.fetchTopRatedMovies();
        if (topRatedMovies.isEmpty()) {
            displayMessage("Brak najlepiej ocenianych filmów.");
        } else {
            for (Movie movie : topRatedMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    // Wyświetlenie ulubionych filmów
    private void displayFavoriteMovies() {
        List<Movie> favoriteMovies = movieController.getFavoriteMovies();
        if (favoriteMovies.isEmpty()) {
            displayMessage("Brak ulubionych filmów.");
        } else {
            for (Movie movie : favoriteMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    // Wyświetlenie detali filmu
    private void displayMovieDetails(Movie movie) {
        String message = String.format("Tytuł: %s\nReżyser: %s\nData produkcji: %s\nOpis: %s\nOcena: %s\n",
                movie.getTitle(), movie.getDirector(), movie.getReleaseDate(), movie.getOverview(), movie.getRating());
        displayMessage(message);
    }

    // Wyświetlenie wiadomości dla użytkownika
    public void displayMessage(String message) {
        Panel messagePanel = new Panel();
        messagePanel.addComponent(new Label(message));

        Button okButton = new Button("OK", () -> {
            // Zamknięcie ostatniego BasicWindow
            Collection<Window> windows = textGUI.getWindows();
            BasicWindow lastWindow = null;

            // Znalezienie ostatniego BasicWindow w kolekcji
            for (Window window : windows) {
                if (window instanceof BasicWindow) {
                    lastWindow = (BasicWindow) window;
                }
            }

            // Usunięcie BasicWindow
            if (lastWindow != null) {
                textGUI.removeWindow(lastWindow);
            }
        });

        messagePanel.addComponent(okButton);

        BasicWindow messageWindow = new BasicWindow("Informacja");
        messageWindow.setComponent(messagePanel);

        textGUI.addWindowAndWait(messageWindow);
    }

    // Okno do wprowadzenia danych od użytkownika
    protected String promptForInput(String prompt) {
        Panel inputPanel = new Panel();
        inputPanel.addComponent(new Label(prompt));

        TextBox inputBox = new TextBox();
        inputPanel.addComponent(inputBox);

        Button okButton = new Button("OK", () -> {
            // Zamknięcie ostatniego BasicWindow
            Collection<Window> windows = textGUI.getWindows();
            BasicWindow lastWindow = null;

            // Znalezienie ostatniego BasicWindow w kolekcji
            for (Window window : windows) {
                if (window instanceof BasicWindow) {
                    lastWindow = (BasicWindow) window;
                }
            }

            // Usunięcie BasicWindow
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

    // Zamknięcie aplikacji
    private void exitApplication() {
        System.exit(0);
    }
}
