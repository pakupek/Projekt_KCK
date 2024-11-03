package org.example;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import java.util.Collections;
import java.util.List;

public class OmdbService  {
    private final String API_KEY = "85fcb8bf";
    private final String BASE_URL = "http://www.omdbapi.com/?apikey=" + API_KEY;


    public Movie fetch_Movie_Details(String title) {
        HttpResponse<JsonNode> response = Unirest.get(BASE_URL)
                .queryString("t", title)
                .asJson();

        if (response.getStatus() == 200) {
            String json = response.getBody().toString();
            return parseMovie(json);
        } else {
            System.out.println("Film nie został znaleziony");
            return null;
        }
    }


    public List<Movie> fetch_NowPlaying_Movies() {
        // Zwróć pustą listę, bo ta funkcjonalność nie jest obsługiwana przez OMDb
        return Collections.emptyList();
    }

    private Movie parseMovie(String json) {
        // Implementacja parsowania JSON do obiektu Movie
        String title = extractValue(json, "Title");
        String director = extractValue(json, "Director");
        String year = extractValue(json, "Year");
        String rating = extractValue(json, "imdbRating");

        return new Movie(title, director, year, rating, "Opis niedostępny");
    }

    private String extractValue(String json, String key) {
        int keyIndex = json.indexOf(key);
        int startIndex = json.indexOf(':', keyIndex) + 1;
        int endIndex = json.indexOf(',', startIndex);
        if (endIndex == -1) endIndex = json.indexOf('}', startIndex);
        return json.substring(startIndex, endIndex).replaceAll("\"", "").trim();
    }
}
