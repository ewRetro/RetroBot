package com.retrogray.retrobot.command.commands.admin;

import com.retrogray.retrobot.CstPrefix;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.database.DatabaseManager;
import com.retrogray.retrobot.database.SQLiteDataSource;
import com.retrogray.retrobot.utils.SQLiteReconnect;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        SQLiteReconnect reconnect = new SQLiteReconnect();
        reconnect.reconnect();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You do not have permissions to use this command! (MANAGE_SERVER)")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        if (ctx.getArgs().isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Missing arguments!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);


        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription(":white_check_mark: New prefix set as: `" + newPrefix + "`");
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "setprefix";
    }


    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Set a custom prefix for the server\n" +
                        "Usage: rb!prefix [newPrefix]")
                .setColor(0xffcc00);
        return embed.build();
    }

    private void updatePrefix(long guildId, String newPrefix) {
        CstPrefix.PREFIXES.put(guildId, newPrefix);
        DatabaseManager.INSTANCE.setPrefix(guildId, newPrefix);

    }
}