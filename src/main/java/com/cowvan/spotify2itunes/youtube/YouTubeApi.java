package com.cowvan.spotify2itunes.youtube;

import com.cowvan.spotify2itunes.Constants;
import com.cowvan.spotify2itunes.Spotify2iTunes;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class YouTubeApi {
    private final Console console;

    public YouTubeApi(Console console) {
        this.console = console;
    }

    public boolean downloadSong(String url) throws IOException, InterruptedException {
        Process ytdlpProcess = new ProcessBuilder(Constants.ytdlpWorstVideoBestVideo + " \"" + url + "\"")
                .redirectErrorStream(true)
                .start();
        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));
        String ytdlpProcessInputStreamLine;

        console.printf(String.valueOf(ytdlpInputStreamReader.readLine() != null));

        while ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {
            console.printf(ytdlpProcessInputStreamLine + "\n ");
        }

        ytdlpProcess.waitFor();

        ytdlpInputStreamReader.close();

        return ytdlpProcess.exitValue() == 0;
    }
}