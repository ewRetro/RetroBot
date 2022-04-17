package com.retrogray.retrobot.command.commands.info;

import com.retrogray.retrobot.CommandManager;
import com.retrogray.retrobot.Config;
import com.retrogray.retrobot.CstPrefix;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommand implements ICommand {

    private final CommandManager manager;

    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }


    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            String prefix = CstPrefix.PREFIXES.get(ctx.getGuild().getIdLong());
            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it) -> builder.append("**")
                            .append(prefix)
                            .append(it)
                            .append("**\n")
            );
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setTitle("List of commands")
                    .setDescription("Current guild prefix: " +
                            prefix +
                            "\n" +
                            builder.toString());
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Nothing found for " + search)
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        channel.sendMessageEmbeds(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }


    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Get help on what a command does and how to use it, or just the command list!\n" +
                        "Usage: rb!help [command]")
                .setColor(0xffcc00);
        return embed.build();
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }
}
