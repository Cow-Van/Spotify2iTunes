package com.cowvan.spotify2itunes.spotify;

public class Song {
    private final String title;
    private final String[] artists;
    private final String image;

    public Song(String title, String[] artists, String image) {
        this.title = title;
        this.artists = artists;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String[] getArtists() {
        return artists;
    }

    public String getImage() {
        return image;
    }
}
