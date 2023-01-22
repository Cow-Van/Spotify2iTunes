package com.cowvan.spotify2itunes.command;

import com.cowvan.spotify2itunes.command.argument.Flag;
import com.cowvan.spotify2itunes.command.argument.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandTest {
    @Test
    public void test_CommandBuilder_worstAudioBestVideo() {
        String ytdlpWorstAudioBestVideo =
                new Command.CommandBuilder("yt-dlp")
                        .addOption(Option.literal("--format", "\"wv+ba\""))
                        .addFlag(Flag.literal("--extract-audio"))
                        .addOption(Option.literal("--audio-format", "\"wav\""))
                        .addOption(Option.literal("--audio-quality", "\"0\""))
                        .addFlag(Flag.literal("--dump-json"))
                        .addFlag(Flag.literal("--no-simulate"))
                        .addOption(Option.literal("--progress-template", """
                                                                         '{"progressPercentage": "%(progress._percent_str)s", "progressTotal": "%(progress._total_bytes_str)s", "speed":"%(progress._speed_str)s", "ETA": "%(progress._eta_str)s"}'
                                                                         """))
                        .build()
                        .asString();

        assertEquals(ytdlpWorstAudioBestVideo, """
                yt-dlp --format "wv+ba" --extract-audio --audio-format "wav" --audio-quality "0" --dump-json --no-simulate --progress-template '{"progressPercentage": "%(progress._percent_str)s", "progressTotal": "%(progress._total_bytes_str)s", "speed":"%(progress._speed_str)s", "ETA": "%(progress._eta_str)s"}'
                """);
    }
}