package com.retrogray.retrobot.command.commands.admin;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class BanCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if(args.size() < 1) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You must mention a user!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        } else {
            long targetID = Long.parseLong(args.get(0).replaceAll("<@", "").replaceAll(">", "").replaceAll("!", ""));
            Member target = ctx.getMessage().getMentionedMembers().get(0);
            if(target == null) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | This user was not found in this server!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }
            Member moderator = ctx.getMember();
            //Remember to check if the modertor canInteract with the target as well!
            if(!moderator.hasPermission(Permission.BAN_MEMBERS) || !moderator.canInteract(target)) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | You are not an admin/moderator on this server!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            if(!ctx.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Bot is missing permissions! (`BAN_MEMBERS`)")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            if(target.hasPermission(Permission.BAN_MEMBERS) || !ctx.getGuild().getSelfMember().canInteract(target)) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | I can't ban this user because they either have a role that is higher than mine or are a moderator!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            int days = 0;
            String reason = "";
            try {
                days = Integer.parseInt(args.get(1));
            } catch(Exception e) {}
            try {
                if(days == 0) {
                    reason = String.join(" ", args.subList(1, args.size()));
                } else {
                    reason = String.join(" ", args.subList(2, args.size()));
                }
            } catch(Exception e) {}
            try {
                target.ban(days == 0 ? days : 0, reason == "" ? null : reason).queue();
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle(":hammer: | Successfully banned user `" +target.getAsMention()+"`")
                        .setDescription("**Banned by: **"+ ctx.getMessage().getAuthor().getAsMention() +"\n"+
                                "**Reason: **`"+ reason + "`\n" +
                                "**Duration of ban: **`"+days+"`");
                channel.sendMessageEmbeds(embed.build()).queue();
            } catch(Exception e) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Ban failed!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        }
    }


    @Override
    public String getName() {
        return "ban";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
