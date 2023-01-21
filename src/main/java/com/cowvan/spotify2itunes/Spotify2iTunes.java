package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.itunes.BetteriTunes;
import com.cowvan.spotify2itunes.spotify.SpotifyApi;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class Spotify2iTunes {
    public static final Console CONSOLE = System.console();

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        BetteriTunes iTunes = new BetteriTunes();
        SpotifyApi spotifyApi = new SpotifyApi(System.getenv("SPOTIFY_CLIENT_ID"), System.getenv("SPOTIFY_CLIENT_SECRET"));

        if (CONSOLE == null && !GraphicsEnvironment.isHeadless()) {
            String filename = Spotify2iTunes.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
            System.exit(0);
            return;
        } else if (CONSOLE == null) {
            throw new NullPointerException("Console is null");
        }

        try {
            new ProcessBuilder(Constants.ytdlpTest)
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
            Process wingetProcess = new ProcessBuilder("winget", "install", "yt-dlp", "--accept-package-agreements", "--accept-source-agreements")
                    .redirectErrorStream(true)
                    .start();

            BufferedReader wingetInputStreamReader = new BufferedReader(new InputStreamReader(wingetProcess.getInputStream()));
            String wingetProcessInputStreamLine;

            while ((wingetProcessInputStreamLine = wingetInputStreamReader.readLine()) != null) {
                CONSOLE.printf(wingetProcessInputStreamLine + "\n ");
            }

            wingetProcess.waitFor();

            wingetInputStreamReader.close();

            new ProcessBuilder(Constants.ytdlpTest)
                    .redirectErrorStream(true)
                    .start();
        }

        System.out.println(spotifyApi.getPlaylist("2efuy7K4TDtksyc4MalYHE"));
    }
}
