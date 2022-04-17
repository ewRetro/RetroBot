package com.retrogray.retrobot.command.commands.info;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.format.DateTimeFormatter;

public class GuildInfoCommand implements ICommand {

    private final static String LINESTART = "\u25AB"; // ▫
    private final static String GUILD_EMOJI = "\uD83D\uDDA5"; // 🖥
    private final static String NO_REGION = "\u2754"; // ❔
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        Guild guild = ctx.getGuild();
        Member owner = guild.getOwner();
        TextChannel channel = ctx.getChannel();
        long onlineCount = guild.getMembers().stream().filter(u -> u.getOnlineStatus() != OnlineStatus.OFFLINE).count();
        long botCount = guild.getMembers().stream().filter(m -> m.getUser().isBot()).count();
        EmbedBuilder builder = new EmbedBuilder();
        String title = (GUILD_EMOJI + " Information about **" + guild.getName() + "**:")
                .replace("@everyone", "@\u0435veryone") // cyrillic e
                .replace("@here", "@h\u0435re") // cyrillic e
                .replace("discord.gg/", "dis\u0441ord.gg/"); // cyrillic c;
        String verif;
        switch(guild.getVerificationLevel())
        {
            case VERY_HIGH:
                verif = "┻━┻ミヽ(ಠ益ಠ)ノ彡┻━┻";
                break;
            case HIGH:
                verif = "(╯°□°）╯︵ ┻━┻";
                break;
            default:
                verif = guild.getVerificationLevel().name();
                break;
        }
        String str = LINESTART + "ID: **" + guild.getId() + "**\n"
                + LINESTART + "Owner: " + (owner == null ? "Unknown" : "**" + owner.getUser().getName() + "**#" + owner.getUser().getDiscriminator()) + "\n"
                + LINESTART + "Location: " + (guild.getLocale().getCountry().isEmpty() ? NO_REGION : guild.getLocale().getCountry()) + " **" + guild.getLocale().getCountry() + "**\n"
                + LINESTART + "Creation: **" + guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME) + "**\n"
                + LINESTART + "Users: **" + guild.getMemberCache().size() + "** (" + onlineCount + " online, " + botCount + " bots)\n"
                + LINESTART + "Channels: **" + guild.getTextChannelCache().size() + "** Text, **" + guild.getVoiceChannelCache().size() + "** Voice, **" + guild.getCategoryCache().size() + "** Categories\n"
                + LINESTART + "Verification: **" + verif + "**";
        if(!guild.getFeatures().isEmpty())
            str += "\n" + LINESTART + "Features: **" + String.join("**, **", guild.getFeatures()) + "**";
        if(guild.getSplashId() != null)
        {
            builder.setImage(guild.getSplashUrl() + "?size=1024");
            str += "\n" + LINESTART + "Splash: ";
        }
        if(guild.getIconUrl()!=null)
            builder.setThumbnail(guild.getIconUrl());
        builder.setColor(owner == null ? null : owner.getColor());
        builder.setDescription(str);
        channel.sendMessage(new MessageBuilder().append(title).setEmbeds(builder.build()).build()).queue();

    }

    @Override
    public String getName() {
        return "serverinfo";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
