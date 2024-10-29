package org.example;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        OmdbService omdbService = new OmdbService();
        TmdbService tmdbService = new TmdbService();
        MovieView view = new MovieView();
        
        MovieController controller = new MovieController(omdbService, tmdbService, view);
        controller.run();
    }
}