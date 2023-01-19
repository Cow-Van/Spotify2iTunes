package com.cowvan.spotify2itunes.command;

import com.cowvan.spotify2itunes.command.argument.Argument;
import com.cowvan.spotify2itunes.command.argument.Flag;
import com.cowvan.spotify2itunes.command.argument.Option;
import com.cowvan.spotify2itunes.command.argument.Word;

import java.util.ArrayList;

public class Command {
    private final String command;

    private Command(CommandBuilder builder) {
        StringBuilder stringBuilder = new StringBuilder(builder.commandName);

        for (Argument argument : builder.arguments) {
            stringBuilder.append(" ").append(argument.asString());
        }

        command = stringBuilder.toString();
    }

    public String asString() {
        return command;
    }

    public static class CommandBuilder {
        private final ArrayList<Argument> arguments = new ArrayList<>();
        private String commandName;

        public CommandBuilder(String commandName) {
            this.commandName = commandName;
        }

        public CommandBuilder addWord(Word word) {
            arguments.add(word);
            return this;
        }

        public CommandBuilder addOption(Option option) {
            arguments.add(option);
            return this;
        }

        public CommandBuilder addFlag(Flag flag) {
            arguments.add(flag);
            return this;
        }

        // TODO
        public CommandBuilder and(CommandBuilder command) {
            return this;
        }

        // TODO
        public CommandBuilder or(CommandBuilder command) {
            return this;
        }

        public CommandBuilder setCommandName(String commandName) {
            this.commandName = commandName;
            return this;
        }

        // TODO
        public CommandBuilder removeFlag(Flag flag) {
            return this;
        }

        // TODO
        public CommandBuilder removeWord(Word word) {
            return this;
        }

        // TODO
        public CommandBuilder removeOption(Option option) {
            return this;
        }

        // TODO
        public CommandBuilder clearArguments() {
            return this;
        }

        // TODO
        public CommandBuilder clearFlags() {
            return this;
        }

        // TODO
        public CommandBuilder clearWords() {
            return this;
        }

        // TODO
        public CommandBuilder clearOptions() {
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
