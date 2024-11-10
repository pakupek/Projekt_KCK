package org.example;
import java.io.*;
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
        loadFavoriteMovies();  // Ładowanie ulubionych filmów przy starcie aplikacji
    }

    //Ustawienie widoku
    public void setView(MovieView movieView) {
        this.view = movieView;
    }

    // Metoda do zapisywania ulubionych filmów do pliku tekstowego (JSON)
    public void saveFavoriteMovies() {
        try (FileWriter file = new FileWriter("favoriteMovies.txt")) {
            JSONArray jsonArray = new JSONArray();
            for (Movie movie : favoriteMovies) {
                JSONObject movieJson = new JSONObject();
                movieJson.put("title", movie.getTitle());
                movieJson.put("director", movie.getDirector());
                movieJson.put("releaseDate", movie.getReleaseDate());
                movieJson.put("overview", movie.getOverview());
                movieJson.put("rating", movie.getRating());
                jsonArray.put(movieJson);
            }
            file.write(jsonArray.toString());
            view.displayMessage("Ulubione filmy zostały zapisane.");
        } catch (IOException e) {
            e.printStackTrace();
            view.displayMessage("Błąd zapisywania do pliku.");
        }
    }

    // Metoda do ładowania ulubionych filmów z pliku tekstowego (JSON)
    private void loadFavoriteMovies() {
        File file = new File("favoriteMovies.txt");
        if (!file.exists()) {
            return;  // Jeśli plik nie istnieje, nie ładować danych
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJson = jsonArray.getJSONObject(i);
                String title = movieJson.getString("title");
                String director = movieJson.optString("director", "Dane niedostępne");
                String releaseDate = movieJson.optString("releaseDate", "Dane niedostępne");
                String overview = movieJson.optString("overview", "Opis niedostępny");
                String rating = movieJson.optString("rating", "Dane niedostępne");

                Movie movie = new Movie(title, director, releaseDate, overview, rating);
                favoriteMovies.add(movie);
            }
        } catch (IOException | org.json.JSONException e) {
            e.printStackTrace();
            view.displayMessage("Błąd ładowania danych z pliku.");
        }
    }

    // Detale wyszukanego filmu
    public Movie fetchMovieDetails(String title) {
        HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/search/movie")
                .queryString("language","en-US")
                .queryString("page",1)
                .queryString("query", title)
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .asString();

        if (response.getStatus() == 200) {
            String json = response.getBody();

            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            // Sprawdzenie czy wynik jest pusty
            if (results.length() == 0) {
                System.out.println("Film o tytule '" + title + "' nie został znaleziony.");
                return null;
            }

            // Pobieranie wyników
            JSONObject movieJson = results.getJSONObject(0);  // Indeks 0 ponieważ użytkownik szuka jednego filmu
            String movieTitle = movieJson.optString("title", "Dane niedostępne");
            String director = "Dane niedostępne"; // TMDb nie zwraca tego wyniku
            String releaseDate = movieJson.optString("release_date", "Dane niedostępne").substring(0, 4);
            String rating = movieJson.optString("vote_average", "Dane niedostępne");
            String overview = movieJson.optString("overview", "Opis niedostępny");

            // Tworzymy i zwracamy obiekt Movie
            return new Movie(movieTitle, director, releaseDate, overview, rating);
        } else {
            System.out.println("Błąd pobierania danych: " + response.getStatus());
            return null;
        }
    }


    //Obecnie grane filmy
    public List<Movie> fetchNowPlayingMovies() {
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
                String director = "Dane niedostępne";
                String releaseDate = movieJson.getString("release_date").substring(0, 4);
                double rating = movieJson.getDouble("vote_average");
                String overview = movieJson.getString("overview");

                Movie movie = new Movie(title, director, releaseDate, overview, String.valueOf(rating));
                movies.add(movie);
            }
        } else {
            System.out.println("Błąd pobierania danych: " + response.getStatus());
        }
        return movies;
    }

    // Najlepiej oceniane filmy
    public List<Movie> fetchTopRatedMovies(){
        HttpResponse<String> response = Unirest.get("https://api.themoviedb.org/3/movie/top_rated")
                .queryString("language", "en-US")
                .queryString("page", 1)
                .header("accept", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .asString();

        List<Movie> movies = new ArrayList<>();
        if (response.getStatus() == 200) {
            String json = response.getBody();
            JSONObject jsonObject = new JSONObject(json);
            JSONArray results = jsonObject.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject movieJson = results.getJSONObject(i);
                String title = movieJson.getString("title");
                String director = "Dane niedostępne";
                String releaseDate = movieJson.getString("release_date").substring(0, 4);
                double rating = movieJson.getDouble("vote_average");
                String overview = movieJson.getString("overview");

                Movie movie = new Movie(title, director, releaseDate, overview,String.valueOf(rating));
                movies.add(movie);
            }
        } else {
            System.out.println("Błąd pobierania danych: " + response.getStatus());
        }
        return movies;
    }

    public List<Movie> getFavoriteMovies() {
        return new ArrayList<>(favoriteMovies); // Zwraca kopię listy ulubionych filmów
    }

    public void addToFavorites() {
        String title = view.promptForInput("Podaj tytuł filmu:");
        Movie movie = fetchMovieDetails(title); // Pobranie szczegółów filmu na podstawie tytułu
        if (movie != null) {
            favoriteMovies.add(movie);
            view.displayMessage("Film '" + title + "' dodany do ulubionych.");
            saveFavoriteMovies();  // Zapisz zmodyfikowaną listę do pliku
        } else {
            view.displayMessage("Film '" + title + "' nie został znaleziony.");
        }
    }
}
