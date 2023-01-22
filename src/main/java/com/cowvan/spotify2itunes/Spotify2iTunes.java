package com.cowvan.spotify2itunes;

import java.io.Console;
import java.util.Arrays;

public class Spotify2iTunes {
    public static final Console CONSOLE = System.console();

    public static void main(String[] args) {
        //        BetteriTunes iTunes = new BetteriTunes();
        //        SpotifyApi spotifyApi = new SpotifyApi(System.getenv("SPOTIFY_CLIENT_ID"), System.getenv("SPOTIFY_CLIENT_SECRET"));
        Spotify2iTunesApp app = new Spotify2iTunesApp(CONSOLE);

        try {
            app.start();
        } catch (Exception e) {
            CONSOLE.printf(e.getMessage());
            CONSOLE.printf(Arrays.toString(e.getStackTrace()));
        }

        while (true) {

        }
    }
}
