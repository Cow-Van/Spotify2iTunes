package com.cowvan.spotify2itunes.spotify;

public record SongData(String title, String[] artists, String album, String[] albumArtists, String imageUrl) {
}