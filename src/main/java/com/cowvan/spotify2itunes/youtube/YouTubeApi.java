package com.cowvan.spotify2itunes.youtube;

import com.cowvan.spotify2itunes.Constants;
import com.cowvan.spotify2itunes.command.Command;
import com.cowvan.spotify2itunes.command.argument.Option;
import com.cowvan.spotify2itunes.command.argument.Word;

import java.io.*;

public class YouTubeApi { // TODO: multithreading search & download
    private final Console console;

    public YouTubeApi(Console console) {
        this.console = console;
    }

    public File downloadSong(String songId) throws IOException, InterruptedException {
        return downloadSong(songId, new File(""), null);
    }

    public File downloadSong(String songId, File downloadDir) throws IOException, InterruptedException {
        return downloadSong(songId, downloadDir, null);
    }

    public File downloadSong(String songId, File downloadDir, String songName) throws IOException, InterruptedException {
        Command.CommandBuilder commandBuilder = new Command.CommandBuilder(Constants.ytdlpWorstVideoBestVideoCommand)
                .addOption(Option.literal("--paths", downloadDir.getAbsolutePath()))
                .addWord(Word.literal("\"%s\"".formatted(songId)));

        if (songName != null) {
            commandBuilder.addOption(Option.literal("--output", "\"%s.%%(ext)s\"".formatted(songName)));
        }

        Process ytdlpProcess = new ProcessBuilder(commandBuilder.build().asArray())
                .redirectErrorStream(true)
                .start();
        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));

        while (ytdlpInputStreamReader.readLine() != null) {

        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return (ytdlpProcess.exitValue() == 0) ? new File(downloadDir, songName + "." + Constants.ytdlpWorstVideoBestVideoFileFormat) : null;
    }

    public String searchSong(String song) throws IOException, InterruptedException {
        String songId = "";
        Command command = new Command.CommandBuilder(Constants.ytdlpSearchCommand)
                .addWord(Word.literal("\"%s\"".formatted(song)))
                .build();

        Process ytdlpProcess = new ProcessBuilder(command.asArray())
                .redirectErrorStream(true)
                .start();
        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));

        String ytdlpProcessInputStreamLine;

        if ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {
            songId = ytdlpProcessInputStreamLine;
        }

        while (ytdlpInputStreamReader.readLine() != null) {

        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return songId;
    }
}
