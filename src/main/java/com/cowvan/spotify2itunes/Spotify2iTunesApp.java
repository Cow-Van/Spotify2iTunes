package com.cowvan.spotify2itunes;

import java.awt.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class Spotify2iTunesApp {
    private final Console console;

    public Spotify2iTunesApp(Console console) {
        this.console = console;
    }

    public void start() throws IOException, InterruptedException {
        checkConsoleExists();
        checkYtdlpExists();
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
            new ProcessBuilder(Constants.ytdlpTest.asArray())
                    .redirectErrorStream(true)
                    .start();
        } catch (IOException e) {
            console.printf(e.getMessage());

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

        new ProcessBuilder(Constants.ytdlpTest.asArray())
                .redirectErrorStream(true)
                .start();
    }
}
