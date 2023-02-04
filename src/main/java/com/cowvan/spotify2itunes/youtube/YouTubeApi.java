package com.cowvan.spotify2itunes.youtube;

import com.cowvan.spotify2itunes.Constants;
import com.cowvan.spotify2itunes.command.Command;
import com.cowvan.spotify2itunes.command.argument.Option;
import com.cowvan.spotify2itunes.command.argument.Word;

import java.io.*;

public class YouTubeApi {
    private final Console console;

    public YouTubeApi(Console console) {
        this.console = console;
    }

    public boolean downloadSong(String songId) throws IOException, InterruptedException {
        return downloadSong(songId, new File(""));
    }

    public boolean downloadSong(String songId, File downloadDir) throws IOException, InterruptedException {
        Command command = new Command.CommandBuilder(Constants.ytdlpWorstVideoBestVideoCommand)
                .addOption(Option.literal("--paths", downloadDir.getAbsolutePath()))
                .addWord(Word.literal("\"%s\"".formatted(songId)))
                .build();

        Process ytdlpProcess = new ProcessBuilder(command.asArray())
                .redirectErrorStream(true)
                .start();
        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));
        String ytdlpProcessInputStreamLine;

        while ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {

        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return ytdlpProcess.exitValue() == 0;
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

        while ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {
            console.printf(ytdlpProcessInputStreamLine + "\n ");
        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return songId;
    }
}
