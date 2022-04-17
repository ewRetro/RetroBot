package com.retrogray.retrobot.command.commands.admin;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PurgeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if(args.size() == 1) {
            if(!ctx.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setDescription(":x: | You do not have permissions to use this command! (`MANAGE_SERVER`)")
                            .setColor(0xff0000);
                    channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            ctx.getMessage().delete().complete();
            int num = 0;
            try {
                num = Integer.parseInt(args.get(0));
            } catch(NumberFormatException nfe) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | You did not provide a number!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }
            int currentNum = num/100;
            if(currentNum == 0) {
                List<Message> msg = ctx.getChannel().getHistory().retrievePast(num).complete();
                ctx.getChannel().purgeMessages(msg);
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":white_check_mark: | Purged `" + num + "` messages!");
                channel.sendMessageEmbeds(embed.build()).queue(message -> message.delete().queueAfter(5L, TimeUnit.SECONDS));
                return;
            }
            try {
                for(int i=0; i<= currentNum; i++) {
                    if(i == num) {
                        List<Message> msg = ctx.getChannel().getHistory().retrievePast(num).complete();
                        ctx.getChannel().purgeMessages(msg);
                        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                                .setDescription(":white_check_mark: | Purged `" + num + "` messages!");
                        channel.sendMessageEmbeds(embed.build()).queue(message -> message.delete().queueAfter(5L, TimeUnit.SECONDS));
                    } else {
                        List<Message> msg = ctx.getChannel().getHistory().retrievePast(100).complete();
                        ctx.getChannel().purgeMessages(msg);
                        num -= 100;
                    }
                }
            } catch(Exception e) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Error encountered while purging, deleted as many messages as possible!");
                channel.sendMessageEmbeds(embed.build()).queue(message -> message.delete().queueAfter(5L, TimeUnit.SECONDS));
                return;
            }
        } else {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You did not provide a number!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "purge";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
