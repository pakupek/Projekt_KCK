package org.example;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieController {

    private final List<Movie> favoriteMovies;
    private final String API_KEY = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4NzEwMzY4MmY0ZWQ0NTI3Y2QzYjYzZDM1NTcyZmU3MiIsIm5iZiI6MTczMDIwMzg1Ni41NzMwMDMsInN1YiI6IjYyOWY4ODY3ZDIxNDdjMTE3ZTYzZTNlYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.2uMrsVqkiYb7BFd_yCBX4TQ2xdEG8IiTrkkOw-2U5QM";
    private final String BASE_URL = "https://api.themoviedb.org/3";
    private MovieView view;

    public MovieController() {
        this.favoriteMovies = new ArrayList<>();

    }

    public void setView(MovieView movieView) {
        this.view = movieView;
    }


    public Movie fetchMovieDetails(String title) {  // Detale wyszukanego filmu
        HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/search/movie")
                .queryString("language","en-US")
                .queryString("page",1)
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY) // Użyj swojego klucza API
                .asString();

        if (response.getStatus() == 200) {
            String json = response.getBody();
            return parseMovie(json);
        } else {
            System.out.println("Film nie został znaleziony");
            return null;
        }
    }



    public List<Movie> fetchNowPlayingMovies() {    //Obecnie grane
        HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/now_playing")
                .queryString("language","en-US")
                .queryString("page",1)
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY) // Użyj swojego klucza API
                .asString();

        List<Movie> movies = new ArrayList<>();
        if (response.getStatus() == 200) {
            String json = response.getBody();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                String title = movieJson.getString("title");
                String director = ""; // Możesz dodać pole dla reżysera, jeśli masz te dane
                String releaseDate = movieJson.getString("release_date").substring(0, 4);
                double rating = movieJson.getDouble("vote_average");
                String overview = movieJson.getString("overview");

                Movie movie = new Movie(title, director, releaseDate, String.valueOf(rating),overview);
                movies.add(movie);
            }
        } else {
            System.out.println("Błąd pobierania danych: " + response.getStatus());
        }
        return movies;
    }

    public List<Movie> fetchTopRatedMovies(){
        HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/top_rated")
                .queryString("language", "en-US")
                .queryString("page", 1)
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY) // Użyj swojego klucza API
                .asString();
        List<Movie> movies = new ArrayList<>();
        if (response.getStatus() == 200) {
            String json = response.getBody();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                String title = movieJson.getString("title");
                String director = ""; // Możesz dodać pole dla reżysera, jeśli masz te dane
                String releaseDate = movieJson.getString("release_date").substring(0, 4);
                double rating = movieJson.getDouble("vote_average");
                String overview = movieJson.getString("overview");

                Movie movie = new Movie(title, director, releaseDate, String.valueOf(rating),overview);
                movies.add(movie);
            }
        } else {
            System.out.println("Błąd pobierania danych: " + response.getStatus());
        }
        return movies;
    }

    private Movie parseMovie(String json) {
        // Implementacja parsowania JSON do obiektu Movie
        String title = extractValue(json, "title");
        String director = "Dane niedostępne"; // TMDb może nie zwracać reżysera w wynikach wyszukiwania
        String year = extractValue(json, "release_date");
        String rating = extractValue(json, "vote_average");

        return new Movie(title, director, year, rating, "Opis niedostępny");
    }

    private String extractValue(String json, String key) {
        int keyIndex = json.indexOf(key);
        int startIndex = json.indexOf(':', keyIndex) + 1;
        int endIndex = json.indexOf(',', startIndex);
        if (endIndex == -1) endIndex = json.indexOf('}', startIndex);
        return json.substring(startIndex, endIndex).replaceAll("\"", "").trim();
    }

    public List<Movie> getFavoriteMovies() {
        return new ArrayList<>(favoriteMovies); // Zwróć kopię listy ulubionych filmów
    }

    public void addToFavorites() {
        String title = view.getMovieTitle(); // Uzyskaj tytuł od użytkownika
        Movie movie = fetchMovieDetails(title); // Pobierz szczegóły filmu na podstawie tytułu
        if (movie != null) {
            favoriteMovies.add(movie); // Dodaj film do ulubionych
            view.displayMessage("Film '" + title + "' dodany do ulubionych."); // Wyświetl komunikat potwierdzający
        } else {
            view.displayMessage("Film '" + title + "' nie został znaleziony."); // Wyświetl komunikat o błędzie
        }
    }
}
