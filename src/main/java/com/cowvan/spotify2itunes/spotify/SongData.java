package com.cowvan.spotify2itunes.spotify;

public record SongData(String title, String validFilenameTitle, String[] artists, String[] validFilenameArtists, String album, String validFilenameAlbum, String[] albumArtists,
                       String[] validFilenameAlbumArtists, String imageUrl) {
}