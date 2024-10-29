package org.example;

import java.util.List;

public interface MovieService {
    // Metoda do wyszukiwania szczegółów filmu na podstawie tytułu
    Movie fetchMovieDetails(String title);

    // Opcjonalna metoda do wyświetlania obecnie granych filmów, implementowana tylko przez TmdbService
    List<Movie> fetchNowPlayingMovies();
}
