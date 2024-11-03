package org.example;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Tworzymy instancję MovieController (bez przekazywania MovieView)
            MovieController movieController = new MovieController();

            // Tworzymy instancję MovieView i przekazujemy kontroler
            MovieView movieView = new MovieView(movieController);

            // Przekazujemy widok do kontrolera
            movieController.setView(movieView);

            // Uruchamiamy aplikację
            movieView.run();
        } catch (Exception e) {
            e.printStackTrace(); // Obsługuje wyjątki, jeśli coś pójdzie nie tak
        }
    }

}
