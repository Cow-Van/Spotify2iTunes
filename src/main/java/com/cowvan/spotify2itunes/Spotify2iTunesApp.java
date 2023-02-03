package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.itunes.BetteriTunes;
import com.cowvan.spotify2itunes.spotify.Song;
import com.cowvan.spotify2itunes.spotify.SpotifyApi;
import com.cowvan.spotify2itunes.utils.ParseUtils;
import com.cowvan.spotify2itunes.youtube.YouTubeApi;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Objects;

public class Spotify2iTunesApp {
    private final Console console;
    private final SpotifyApi spotifyApi;
    private final YouTubeApi youTubeApi;
    private final BetteriTunes iTunes;

    public Spotify2iTunesApp(Console console, SpotifyApi spotifyApi, YouTubeApi youTubeApi, BetteriTunes iTunes) {
        this.console = console;
        this.spotifyApi = spotifyApi;
        this.youTubeApi = youTubeApi;
        this.iTunes = iTunes;
    }

    public void run() throws IOException, InterruptedException, URISyntaxException {
        checkConsoleExists();
        checkYtdlpExists();

        String link = getSpotifyLinkInput();
        String id = ParseUtils.parseSpotifyLinkToId(link);

        Song[] songs = spotifyApi.getPlaylistSongs(id);

        for (Song song : songs) {
            boolean songDownloaded = false;

            while (!songDownloaded) {
                String songId = youTubeApi.searchSong(song.title() + " - " + String.join(", ", song.artists()));
                songDownloaded = youTubeApi.downloadSong(songId);
            }
        }
    }

    private String getSpotifyLinkInput() {
        String link = null;

        while (Objects.equals(ParseUtils.parseSpotifyLinkToId(link), "")) {
            if (link != null) {
                console.printf("Invalid link\n");
            }

            console.printf("Enter a valid Spotify link to a song or public playlist: ");
            link = console.readLine();
        }

        return link;
    }

    private void checkConsoleExists() throws IOException {
        if (console == null && !GraphicsEnvironment.isHeadless()) {
            String filename = Spotify2iTunes.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
            System.exit(0);
        } else if (console == null) {
            throw new NullPointerException("Console is null");
        }
    }

    private void checkYtdlpExists() throws IOException, InterruptedException {
        try {
            new ProcessBuilder(Constants.ytdlpTestCommand.asArray())
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
            console.printf(e.getMessage() + "\n");

            installYtdlp();
        }
    }

    private void installYtdlp() throws IOException, InterruptedException {
        Process wingetProcess = new ProcessBuilder(Constants.wingetInstallYtdlpCommand.asArray())
                .redirectErrorStream(true)
                .start();

        BufferedReader wingetInputStreamReader = new BufferedReader(new InputStreamReader(wingetProcess.getInputStream()));
        String wingetProcessInputStreamLine;

        while ((wingetProcessInputStreamLine = wingetInputStreamReader.readLine()) != null) {
            console.printf(wingetProcessInputStreamLine + "\n ");
        }

        wingetProcess.waitFor();

        wingetInputStreamReader.close();

        new ProcessBuilder(Constants.ytdlpTestCommand.asArray())
                .redirectErrorStream(true)
                .start();
    }
}
