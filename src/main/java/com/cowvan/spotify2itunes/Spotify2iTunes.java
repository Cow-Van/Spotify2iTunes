package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.itunes.BetteriTunes;
import com.cowvan.spotify2itunes.spotify.SpotifyApi;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;

public class Spotify2iTunes {
    public static final Console CONSOLE = System.console();

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
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
