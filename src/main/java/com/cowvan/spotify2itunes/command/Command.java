package com.cowvan.spotify2itunes.command;

import com.cowvan.spotify2itunes.command.argument.Argument;
import com.cowvan.spotify2itunes.command.argument.Flag;
import com.cowvan.spotify2itunes.command.argument.Option;
import com.cowvan.spotify2itunes.command.argument.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Command {
    private final String commandName;
    private final Argument[] arguments;

    private Command(CommandBuilder builder) {
        commandName = builder.commandName;
        arguments = builder.arguments.toArray(Argument[]::new);
    }

    public String asString() {
        StringBuilder stringBuilder = new StringBuilder(commandName);

        for (Argument argument : arguments) {
            stringBuilder.append(" ");
            stringBuilder.append(argument.asString());
        }

        return stringBuilder.toString();
    }

    public String[] asArray() {
        ArrayList<String> command = new ArrayList<>();
        command.add(commandName);

        for (Argument argument : arguments) {
            command.addAll(List.of(argument.asArray()));
        }

        return command.toArray(String[]::new);
    }

    public static class CommandBuilder {
        private final ArrayList<Argument> arguments = new ArrayList<>();
        private String commandName;

        public CommandBuilder(String commandName) {
            if (commandName.length() < 1) {
                throw new IllegalArgumentException("Command name cannot be empty");
            }

            this.commandName = commandName;
        }

        public CommandBuilder(Command command) {
            this.commandName = command.commandName;
            this.arguments.addAll(Arrays.stream(command.arguments).toList());
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

        public CommandBuilder setCommandName(String commandName) {
            if (commandName.length() < 1) {
                throw new IllegalArgumentException("Command name cannot be empty");
            }

            this.commandName = commandName;
            return this;
        }

        public CommandBuilder removeFlag(Flag flag) {
            arguments.remove(flag);
            return this;
        }

        public CommandBuilder removeWord(Word word) {
            arguments.remove(word);
            return this;
        }

        public CommandBuilder removeOption(Option option) {
            arguments.remove(option);
            return this;
        }

        public CommandBuilder clearArguments() {
            arguments.clear();
            return this;
        }

        public CommandBuilder clearFlags() {
            return clearArgumentType(Flag.class);
        }

        public CommandBuilder clearWords() {
            return clearArgumentType(Word.class);
        }

        public CommandBuilder clearOptions() {
            return clearArgumentType(Option.class);
        }

        public Command build() {
            return new Command(this);
        }

        private CommandBuilder clearArgumentType(Class<?> argumentType) {
            ListIterator<Argument> argumentsIterator = arguments.listIterator();

            while (argumentsIterator.hasNext()) {
                Argument argument = argumentsIterator.next();
                if (argumentType.isInstance(argument)) {
                    arguments.remove(argument);
                    argumentsIterator = arguments.listIterator(argumentsIterator.nextIndex());
                }
            }

            return this;
        }
    }
}
