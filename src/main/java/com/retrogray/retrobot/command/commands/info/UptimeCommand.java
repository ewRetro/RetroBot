package com.retrogray.retrobot.command.commands.info;

import com.retrogray.retrobot.Bot;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

public class UptimeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptime = runtimeMXBean.getUptime();

        JDA jda = ctx.getJDA();

        long uptimeInSeconds = uptime / 1000;
        long numberOfHours = uptimeInSeconds / (60 * 60);
        long numberOfMinutes = (uptimeInSeconds / 60) - (numberOfHours * 60);
        long numberOfSeconds = uptimeInSeconds % 60;
        long numberOfDays = TimeUnit.HOURS.toDays(numberOfHours);

        

        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle("Uptime Info")
                .setDescription(
                        "**Day/s**: `" + numberOfDays + "`\n" +
                        "**Hour/s**: `" + numberOfHours + "`\n" +
                        "**Minute/s**: `" + numberOfMinutes + "`\n" +
                        "**Second/s**: `" + numberOfSeconds + "`")
                .setFooter("RetroBot is in " + jda.getGuildCache().size() + " guilds!");
        channel.sendMessageEmbeds(embed.build()).queue();
        }



    @Override
    public String getName() {
        return "uptime";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}