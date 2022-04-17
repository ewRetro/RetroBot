package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class RepeatCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | I need to be in a voice channel in order to execute this command!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You need to be in a voice channel in order to execute this command!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | I'm not in the same voice channel as you!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;

        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription(":repeat: | The player will now **" + (newRepeating ? "repeat" + "** the current song." : "stop repeating" + "** the current song."));
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "loop";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Loops the current track.\n" +
                        "Usage: <prefix>loop")
                .setColor(0xffcc00);
        return embed.build();
    }

    public List<String> getAliases() {
        return List.of("repeat");

    }

}
