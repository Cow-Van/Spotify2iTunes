package com.cowvan.spotify2itunes.spotify;

public record PlaylistData(String name, String validFilenameName, SongData[] songs) {
}
