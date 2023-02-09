package com.cowvan.spotify2itunes.app;

import com.cowvan.spotify2itunes.Constants;
import com.cowvan.spotify2itunes.Spotify2iTunes;
import com.cowvan.spotify2itunes.itunes.BetterPlaylist;
import com.cowvan.spotify2itunes.itunes.BetteriTunes;
import com.cowvan.spotify2itunes.spotify.PlaylistData;
import com.cowvan.spotify2itunes.spotify.SongData;
import com.cowvan.spotify2itunes.spotify.SpotifyApi;
import com.cowvan.spotify2itunes.utils.ParseUtils;
import com.cowvan.spotify2itunes.youtube.YouTubeApi;
import info.schnatterer.itunes4j.ITunesException;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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

    public void run() throws IOException, InterruptedException, URISyntaxException, ITunesException, CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, CannotWriteException {
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

        Playlist playlist = downloadSongsFromSpotifyPlaylist(id, downloadDir);

        addSongsMetadata(playlist);

        BetterPlaylist iTunesPlaylist = createiTunesPlaylist(playlist.fileLocation().playlist().getName());

        addSongsToiTunesPlaylist(iTunesPlaylist, playlist.fileLocation().playlist());
    }

    private void addSongsToiTunesPlaylist(BetterPlaylist playlist, File songsDir) throws ITunesException {
        console.printf("\nAdding songs to [%s] iTunes playlist...\n\n", songsDir.getName());

        playlist.addFile(songsDir.getAbsolutePath());

        console.printf("All songs added to [%s] iTunes playlist...\n\n", songsDir.getName());
    }

    private BetterPlaylist createiTunesPlaylist(String playlistName) throws ITunesException {
        console.printf("\nCreating [%s] iTunes playlist...\n", playlistName);

        BetterPlaylist playlist = iTunes.createPlaylist(playlistName);

        console.printf("Created [%s] iTunes playlist\n\n", playlistName);

        return playlist;
    }

    private void addSongsMetadata(Playlist playlist) throws CannotReadException, TagException, InvalidAudioFrameException, ReadOnlyFileException, IOException, CannotWriteException {
        for (int i = 0; i < playlist.fileLocation().songs().length; i++) {
            console.printf(playlist.fileLocation().songs()[i].getAbsolutePath());
            AudioFile song = AudioFileIO.read(playlist.fileLocation().songs()[i]);
            Tag songTag = song.getTag();

            songTag.setField(FieldKey.TITLE, playlist.playlistData().songs()[i].title());
            songTag.setField(FieldKey.ARTIST, String.join(", ", playlist.playlistData().songs()[i].artists()));
            songTag.setField(FieldKey.ALBUM, playlist.playlistData().songs()[i].album());
            songTag.setField(FieldKey.ALBUM_ARTIST, String.join(", ", playlist.playlistData().songs()[i].albumArtists()));
//            songTag.setField(FieldKey.LYRICS); TODO

            URL imageUrl = new URL(playlist.playlistData().songs()[i].imageUrl()); // TODO fix
            Path downloadLocation = Path.of(downloadDir.getPath(), "temp", playlist.playlistData().songs()[i].title().replace("/", "_").replace("\\", "_") + "-" + (int) (Math.random() * 1000) + ".jpg");

            downloadImage(imageUrl, downloadLocation);
            songTag.setField(Artwork.createArtworkFromFile(downloadLocation.toFile()));

            song.setTag(songTag);
            AudioFileIO.write(song);
        }

        new File(downloadDir, "temp").deleteOnExit();
    }

    private void downloadImage(URL url, Path destinationFilePath) throws IOException {
        InputStream in = url.openStream();
        Files.copy(in, destinationFilePath);
        in.close();
    }

    public Playlist downloadSongsFromSpotifyPlaylist(String playlistId) throws IOException, InterruptedException, URISyntaxException {
        return downloadSongsFromSpotifyPlaylist(playlistId, new File(""));
    }

    public Playlist downloadSongsFromSpotifyPlaylist(String playlistId, File downloadDir) throws IOException, InterruptedException, URISyntaxException {
        PlaylistData playlistData = spotifyApi.getPlaylistData(playlistId);
        File playlistDir = Paths.get(downloadDir.toPath().toString(), playlistData.name()).toFile();
        ArrayList<File> songFiles = new ArrayList<>();

        console.printf("\nDownloading playlist [%s] at [%s]...\n\n", playlistData.name(), playlistDir.getAbsolutePath());

        for (SongData songData : playlistData.songs()) {
            console.printf("Downloading [%s - %s]...\n", songData.title(), String.join(", ", songData.artists()));

            File songLocation = null;

            while (songLocation == null) {
                String songName = songData.title() + " - " + String.join(", ", songData.artists());
                String songId = youTubeApi.searchSong(songName);
                songLocation = youTubeApi.downloadSong(songId, playlistDir, songName.replace("\\", "_").replace("/", "_"));
            }

            songFiles.add(songLocation);

            console.printf("[%s - %s] has been downloaded\n\n", songData.title(), String.join(", ", songData.artists()));
        }

        console.printf("\n[%s] has been downloaded at [%s]\n\n", playlistData.name(), playlistDir.getAbsolutePath());

        return new Playlist(new FileLocation(playlistDir, songFiles.toArray(File[]::new)), playlistData);
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
