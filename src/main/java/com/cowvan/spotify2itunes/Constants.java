package com.cowvan.spotify2itunes;

import com.cowvan.spotify2itunes.command.Command;
import com.cowvan.spotify2itunes.command.argument.Flag;
import com.cowvan.spotify2itunes.command.argument.Option;
import com.cowvan.spotify2itunes.command.argument.Word;

public final class Constants {
    public static final String wingetYtdlpPackageName = "yt-dlp";
    public static final String ytdlpCommand = "yt-dlp";
    public static final String wingetCommand = "winget";
    public static final Command wingetInstallYtdlpCommand = new Command.CommandBuilder(wingetCommand)
            .addWord(Word.literal("install"))
            .addWord(Word.literal(wingetYtdlpPackageName))
            .addFlag(Flag.literal("--accept-package-agreements"))
            .addFlag(Flag.literal("--accept-source-agreements"))
            .build();
    public static Command ytdlpWorstVideoBestVideo =
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
                    .build();
    public static final Command ytdlpTest = new Command.CommandBuilder("yt-dlp")
            .addFlag(Flag.literal("--version"))
            .build();
}
