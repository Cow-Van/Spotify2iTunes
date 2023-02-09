package com.cowvan.spotify2itunes.app;

import com.cowvan.spotify2itunes.spotify.PlaylistData;

public record Playlist(FileLocation fileLocation, PlaylistData playlistData) {
}
