package org.example;

import java.util.ArrayList;
import java.util.List;

public class MovieController {
    private final OmdbService omdbService;
    private final TmdbService tmdbService;
    private final MovieView view;
    private final List<Movie> favoriteMovies;

    public MovieController(OmdbService omdbService, TmdbService tmdbService, MovieView view) {
        this.omdbService = omdbService;
        this.tmdbService = tmdbService;
        this.view = view;
        this.favoriteMovies = new ArrayList<>();
    }

    public void run() {
        boolean continueRunning = true;

        while (continueRunning) {
            int choice = view.displayMainMenu();

            switch (choice) {
                case 1 -> omdbMenu();
                case 2 -> tmdbMenu();
                case 3 -> continueRunning = false;
                default -> view.displayMessage("Nieprawidłowy wybór.");
            }
        }

        view.displayMessage("Dziękujemy za korzystanie z aplikacji!");
    }

    private void omdbMenu() {
        boolean exitOMDb = false;
        while (!exitOMDb) {
            int choice = view.displaySubMenu("OMDb");

            switch (choice) {
                case 1 -> searchMovie(omdbService);
                case 2 -> view.displayFavoriteMovies(favoriteMovies);
                case 3 -> addToFavorites(omdbService);
                case 4 -> exitOMDb = true;
                default -> view.displayMessage("Nieprawidłowy wybór.");
            }
        }
    }

    private void tmdbMenu() {
        boolean exitTMDb = false;
        while (!exitTMDb) {
            int choice = view.displaySubMenu("TMDb");

            switch (choice) {
                case 1 -> searchMovie(tmdbService);
                case 2 -> displayNowPlaying();
                case 3 -> displayTopRated();
                case 4 -> view.displayFavoriteMovies(favoriteMovies);
                case 5 -> addToFavorites(tmdbService);
                case 6 -> exitTMDb = true;
                default -> view.displayMessage("Nieprawidłowy wybór.");
            }
        }
    }

    private void searchMovie(MovieService service) {
        String title = view.getMovieTitle();
        Movie movie = service.fetchMovieDetails(title);
        if (movie != null) {
            view.displayMovieDetails(movie);
        } else {
            view.displayMessage("Film nie został znaleziony.");
        }
    }

    private void displayNowPlaying() {
        List<Movie> nowPlayingMovies = tmdbService.fetchNowPlayingMovies();
        if (nowPlayingMovies.isEmpty()) {
            view.displayMessage("Brak informacji o obecnie granych filmach.");
        } else {
            nowPlayingMovies.forEach(view::displayMovieDetails);
        }
    }

    private void displayTopRated(){
        List<Movie> topRatedMovies = tmdbService.fetchTopRatedMovies();
        if(topRatedMovies.isEmpty()){
            view.displayMessage("Brak najlepiej ocenianych filmów");
        } else{
            topRatedMovies.forEach(view::displayMovieDetails);
        }
    }

    private void addToFavorites(MovieService service) {
        String title = view.getMovieTitle();
        Movie movie = service.fetchMovieDetails(title);
        if (movie != null) {
            favoriteMovies.add(movie);
            view.displayMessage("Film dodany do ulubionych.");
        } else {
            view.displayMessage("Film nie został znaleziony.");
        }
    }
}
