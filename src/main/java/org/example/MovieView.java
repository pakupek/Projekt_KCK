package org.example;

import java.util.List;
import java.util.Scanner;

public class MovieView {
    private final Scanner scanner = new Scanner(System.in);

    public int displayMainMenu() {
        System.out.println("Wybierz bazę filmową:");
        System.out.println("1. OMDb");
        System.out.println("2. TMDb");
        System.out.println("3. Zakończ działanie programu");
        return scanner.nextInt();
    }

    public int displaySubMenu(String service) {
        System.out.printf("\n%s Menu:\n", service);
        System.out.println("1. Wyszukaj film");
        if ("TMDb".equals(service)) {
            System.out.println("2. Wyświetl listę obecnie granych filmów");
            System.out.println("3. Wyświetl listę najlepiej ocenianych filmów");
            System.out.println("4. Wyświetl ulubione filmy");
            System.out.println("5. Dodaj film do ulubionych");
            System.out.println("6. Powrót do głównego menu");
        } else {
            System.out.println("2. Wyświetl ulubione filmy");
            System.out.println("3. Dodaj film do ulubionych");
            System.out.println("4. Powrót do głównego menu");
        }
        return scanner.nextInt();
    }

    public String getMovieTitle() {
        System.out.println("Podaj tytuł filmu:");
        scanner.nextLine(); // Konsumuje znak nowej linii
        return scanner.nextLine();
    }

    public void displayMovieDetails(Movie movie) {
        System.out.printf("\nTytuł: %s\nReżyser: %s\nData produkcji: %s\nOpis: %s\nOcena: %s\n",
                movie.getTitle(), movie.getDirector(), movie.getReleaseDate(), movie.getRating(), movie.getOverview());
        System.out.println("---------------------------");
    }

    public void displayFavoriteMovies(List<Movie> favoriteMovies) {
        if (favoriteMovies.isEmpty()) {
            System.out.println("Brak ulubionych filmów.");
        } else {
            System.out.println("Ulubione filmy:");
            for (Movie movie : favoriteMovies) {
                displayMovieDetails(movie);
            }
        }
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }
}
