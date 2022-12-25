package com.cowvan.spotify2itunes;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class Spotify2iTunes {
    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
//        BetteriTunes iTunes = new BetteriTunes();
//        SpotifyApi spotifyApi = new SpotifyApi(System.getenv("SPOTIFY_CLIENT_ID"), System.getenv("SPOTIFY_CLIENT_SECRET"));

        Console console = System.console();

        if (console == null && !GraphicsEnvironment.isHeadless()) {
            String filename = Spotify2iTunes.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
            System.exit(0);
            return;
        } else if (console == null) {
            throw new NullPointerException("Console is null");
        }

        Process ytdlpProcess;

        try {
            ytdlpProcess = new ProcessBuilder("yt-dlp").start();
        } catch (IOException e) {
            Process wingetProcess = new ProcessBuilder("winget", "install", "yt-dlp", "--accept-package-agreements", "--accept-source-agreements").start();

            BufferedReader wingetInputStreamReader = new BufferedReader(new InputStreamReader(wingetProcess.getInputStream()));
            String wingetProcessInputStreamLine;

            while ((wingetProcessInputStreamLine = wingetInputStreamReader.readLine()) != null) {
                console.printf(wingetProcessInputStreamLine + "\n ");
            }

            wingetProcess.waitFor();

            wingetInputStreamReader.close();
        }

        while (true) {
            console.printf("ok");
        }
    }
}
