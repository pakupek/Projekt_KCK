package org.example;

public class Movie {
    private String title;
    private String director;
    private String releaseDate;
    private String overview;
    private String rating;

    public Movie(String title, String director, String releaseDate, String overview, String rating) {
        this.title = title;
        this.director = director;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.rating = rating;
    }

    // Getters i Setters
    public String getTitle() { return title; }
    public String getDirector() { return director; }
    public String getReleaseDate() { return releaseDate; }
    public String getOverview() { return overview; }
    public String getRating() { return rating; }
}
