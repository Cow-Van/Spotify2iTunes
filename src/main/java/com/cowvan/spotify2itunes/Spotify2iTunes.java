package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.itunes.BetteriTunes;
import com.cowvan.spotify2itunes.spotify.SpotifyApi;
import com.cowvan.spotify2itunes.youtube.YouTubeApi;

import java.io.Console;
import java.io.IOException;
import java.net.URISyntaxException;

public class Spotify2iTunes {
    public static final Console CONSOLE = System.console();

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        SpotifyApi spotifyApi = new SpotifyApi(System.getenv("SPOTIFY_CLIENT_ID"), System.getenv("SPOTIFY_CLIENT_SECRET"));
        YouTubeApi youTubeApi = new YouTubeApi(CONSOLE);
        BetteriTunes iTunes = new BetteriTunes();

        Spotify2iTunesApp app = new Spotify2iTunesApp(CONSOLE, spotifyApi, youTubeApi, iTunes);

        try {
            app.run();
        } catch (Throwable e) {
            CONSOLE.printf(((e.getMessage() != null) ? e.getMessage().replace("%", "%%") : e.toString().replace("%", "%%")) + "\n");
            e.printStackTrace(CONSOLE.writer());
        }

        while (true) {

        }
    }
}
