package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());

        if (!audioManager.isConnected()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | I'm not in your channel!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You have to be in the same channel as me to use this!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }
        audioManager.closeAudioConnection();
        musicManager.player.stopTrack();
        musicManager.scheduler.getQueue().clear();
        musicManager.scheduler.player.setPaused(false);
        musicManager.scheduler.repeating = false;
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription(":white_check_mark: | Disconnected from your channel!");
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Leave the current channel!\n" +
                        "Usage: rb!leave")
                .setColor(0xffcc00);
        return embed.build();
    }

    public List<String> getAliases() {
        return List.of("l", "dc", "disconnect");
    }

}
