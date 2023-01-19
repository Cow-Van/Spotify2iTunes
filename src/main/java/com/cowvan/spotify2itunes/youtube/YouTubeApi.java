package com.cowvan.spotify2itunes.youtube;

import java.io.IOException;

public class YouTubeApi {
    public YouTubeApi() {
    }

    public static void downloadSong(String name) throws IOException, InterruptedException {
//        Process ytdlpProcess = new ProcessBuilder(Constants.ytdlpWorstVideoBestVideo + " " + name)
//                .redirectErrorStream(true)
//                .start();
//        BufferedReader ytdlpInputStreamReader = new BufferedReader(new InputStreamReader(ytdlpProcess.getInputStream()));
//        String ytdlpProcessInputStreamLine;
//
//        Spotify2iTunes.CONSOLE.printf(String.valueOf(ytdlpInputStreamReader.readLine() != null));
//
//        while ((ytdlpProcessInputStreamLine = ytdlpInputStreamReader.readLine()) != null) {
//            Spotify2iTunes.CONSOLE.printf(ytdlpProcessInputStreamLine + "\n ");
//        }
//
//        ytdlpProcess.waitFor();
//
//        ytdlpInputStreamReader.close();
    }
}
