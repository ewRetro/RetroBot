package com.retrogray.retrobot.command;

import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws InterruptedException;

    String getName();


    MessageEmbed getHelp();

    default List<String> getAliases() {
        return List.of();
    }
}
