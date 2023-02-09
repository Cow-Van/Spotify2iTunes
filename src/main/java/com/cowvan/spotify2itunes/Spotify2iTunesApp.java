package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.itunes.BetterPlaylist;
import com.cowvan.spotify2itunes.itunes.BetteriTunes;
import com.cowvan.spotify2itunes.spotify.Playlist;
import com.cowvan.spotify2itunes.spotify.Song;
import com.cowvan.spotify2itunes.spotify.SpotifyApi;
import com.cowvan.spotify2itunes.utils.ParseUtils;
import com.cowvan.spotify2itunes.youtube.YouTubeApi;
import info.schnatterer.itunes4j.ITunesException;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Objects;

public class Spotify2iTunesApp {
    private final Console console;
    private final SpotifyApi spotifyApi;
    private final YouTubeApi youTubeApi;
    private final BetteriTunes iTunes;

    private File downloadDir = new File(System.getProperty("user.dir"));

    public Spotify2iTunesApp(Console console, SpotifyApi spotifyApi, YouTubeApi youTubeApi, BetteriTunes iTunes) {
        this.console = console;
        this.spotifyApi = spotifyApi;
        this.youTubeApi = youTubeApi;
        this.iTunes = iTunes;
    }

    public void run() throws IOException, InterruptedException, URISyntaxException, ITunesException {
        checkConsoleExists();
        checkYtdlpExists();

        String directory = null;

        while (directory == null) {
            console.printf("Enter a directory path for downloaded files or leave blank to use current working directory: ");
            directory = console.readLine();

            if (directory.equals("")) {
                break;
            }

            if (!isValidPath(directory)) {
                directory = null;
                console.printf("Invalid file path\n");
            } else {
                downloadDir = new File(directory);
            }
        }

        String link = getSpotifyLinkInput();
        String id = ParseUtils.parseSpotifyLinkToId(link);

        File playlistFileLocation = downloadSongsFromSpotifyPlaylist(id, downloadDir);

        console.printf("\nCreating [%s] iTunes playlist...\n", playlistFileLocation.getName());

        BetterPlaylist playlist = iTunes.createPlaylist(playlistFileLocation.getName());

        console.printf("Created [%s] iTunes playlist\n\n", playlistFileLocation.getName());

        console.printf("\nAdding songs to [%s] iTunes playlist...\n\n", playlistFileLocation.getName());

        playlist.addFile(playlistFileLocation.getAbsolutePath());

        console.printf("All songs added to [%s] iTunes playlist...\n\n", playlistFileLocation.getName());
    }

    public File downloadSongsFromSpotifyPlaylist(String playlistId) throws IOException, InterruptedException, URISyntaxException {
        return downloadSongsFromSpotifyPlaylist(playlistId, new File(""));
    }

    public File downloadSongsFromSpotifyPlaylist(String playlistId, File downloadDir) throws IOException, InterruptedException, URISyntaxException {
        Playlist playlist = spotifyApi.getPlaylist(playlistId);
        File playlistDir = new File(downloadDir, playlist.name());

        console.printf("\nDownloading playlist [%s] at [%s]...\n\n", playlist.name(), playlistDir.getAbsolutePath());

        for (Song song : playlist.songs()) {
            console.printf("Downloading [%s - %s]...\n", song.title(), String.join(", ", song.artists()));

            File songLocation = null;

            while (songLocation == null) {
                String songName = song.title() + " - " + String.join(", ", song.artists());
                String songId = youTubeApi.searchSong(songName);
                songLocation = youTubeApi.downloadSong(songId, playlistDir, songName);
            }

            console.printf("[%s - %s] has been downloaded\n\n", song.title(), String.join(", ", song.artists()));
        }

        console.printf("\n[%s] has been downloaded at [%s]\n\n", playlist.name(), playlistDir.getAbsolutePath());

        return playlistDir;
    }

    private String getSpotifyLinkInput() {
        String link = null;

        while (Objects.equals(ParseUtils.parseSpotifyLinkToId(link), "")) {
            if (link != null) {
                console.printf("Invalid link\n");
            }

            console.printf("Enter a valid Spotify link to a public playlist: ");
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
            console.printf(wingetProcessInputStreamLine + "\n");
        }

        wingetProcess.waitFor();

        wingetInputStreamReader.close();

        new ProcessBuilder(Constants.ytdlpTestCommand.asArray())
                .redirectErrorStream(true)
                .start();
    }

    private boolean isValidPath(String path) {
        try {
            Paths.get(path);
        } catch (InvalidPathException | NullPointerException e) {
            return false;
        }
        return true;
    }
}
