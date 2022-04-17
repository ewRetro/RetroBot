package com.retrogray.retrobot.command.commands.info;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class InfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final TextChannel channel = ctx.getChannel();

        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle("Bot Info")
                .setDescription("**Bot Version: **`2.2.0`\n" +
                        "**JDA Version**: `4.4.0_350`\n" +
                        "**Lavaplayer Version:** `1.3.97`\n" +
                        "**Bot Developer:** `Retro#2875`");
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
