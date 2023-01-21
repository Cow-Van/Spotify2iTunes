package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.command.Command;
import com.cowvan.spotify2itunes.command.argument.Flag;
import com.cowvan.spotify2itunes.command.argument.Option;

public final class Constants {
    public static final String wingetYtdlpPackageName = "yt-dlp";
    public static final String ytdlpCommand = "yt-dlp";
    public static final String wingetCommand = "winget";
    public static String ytdlpWorstAudioBestVideo =
            new Command.CommandBuilder(ytdlpCommand)
                    .addOption(Option.literal("--format", "\"wv+ba\""))
                    .addFlag(Flag.literal("--extract-audio"))
                    .addOption(Option.literal("--audio-format", "\"wav\""))
                    .addOption(Option.literal("--audio-quality", "\"0\""))
                    .addFlag(Flag.literal("--dump-single-json"))
                    .addFlag(Flag.literal("--no-simulate"))
                    .addOption(Option.literal("--progress-template", """
                                                                     {"progressPercentage": "%(progress._percent_str)s", "progressTotal": "%(progress._total_bytes_str)s", "speed":"%(progress._speed_str)s", "ETA": "%(progress._eta_str)s"}
                                                                     """))
                    .build()
                    .asString();
    public static String ytdlpTest = new Command.CommandBuilder("yt-dlp")
            .addFlag(Flag.literal("--version"))
            .build()
            .asString();
}
