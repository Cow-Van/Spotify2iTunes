package com.cowvan.spotify2itunes.youtube;

import com.cowvan.spotify2itunes.Constants;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class YouTubeApi {
    private final Console console;

    public YouTubeApi(Console console) {
        this.console = console;
    }

    public boolean downloadSong(String songId) throws IOException, InterruptedException {
        Process ytdlpProcess = new ProcessBuilder(Constants.ytdlpWorstVideoBestVideoCommand.asString() + " \"" + songId + "\"")
                .redirectErrorStream(true)
                .start();
        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));
        String ytdlpProcessInputStreamLine;

        while ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {
            console.printf(ytdlpProcessInputStreamLine + "\n ");
        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return ytdlpProcess.exitValue() == 0;
    }

    public String searchSong(String song) throws IOException, InterruptedException {
        Process ytdlpProcess = new ProcessBuilder(Constants.ytdlpSearchCommand.asString() + " \"" + song + "\"")
                .redirectErrorStream(true)
                .start();
        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));

        String ytdlpProcessInputStreamLine;

        while ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {
            console.printf(ytdlpProcessInputStreamLine + "\n ");
        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return "";
    }
}
