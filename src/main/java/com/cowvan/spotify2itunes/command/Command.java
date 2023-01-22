package com.cowvan.spotify2itunes.command;

import com.cowvan.spotify2itunes.command.argument.Argument;
import com.cowvan.spotify2itunes.command.argument.Flag;
import com.cowvan.spotify2itunes.command.argument.Option;
import com.cowvan.spotify2itunes.command.argument.Word;

import java.util.ArrayList;
import java.util.ListIterator;

public class Command {
    private final String commandName;
    private final Argument[] arguments;

    private Command(CommandBuilder builder) {
        commandName = builder.commandName;
        arguments = builder.arguments.toArray(new Argument[0]);
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
        String[] command = new String[arguments.length + 1];
        command[0] = commandName;

        for (int i = 1; i <= arguments.length; i++) {
            command[i] = arguments[i - 1].asString();
        }

        return command;
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
