package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import com.retrogray.retrobot.lavaplayer.TrackScheduler;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class ResumeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;

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
        if (musicManager.player.getPlayingTrack() != null) {
            if (scheduler.player.isPaused()) {
                    scheduler.player.setPaused(false);
                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setDescription(":arrow_forward: | Music resumed!");
                    channel.sendMessageEmbeds(embed.build()).queue();
                } else {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Player is not paused.")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                }
        } else {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | There is no music playing.")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
