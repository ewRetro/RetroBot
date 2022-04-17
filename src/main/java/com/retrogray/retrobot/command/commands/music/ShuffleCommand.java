package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import com.retrogray.retrobot.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class ShuffleCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        TextChannel channel = ctx.getChannel();
        AudioManager audioManager = ctx.getGuild().getAudioManager();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;

        if (player.getPlayingTrack() == null) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | No songs are playing!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();

            return;
        }

        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription(":white_check_mark: | Queue shuffled!");
        channel.sendMessageEmbeds(embed.build()).queue();
        scheduler.shuffle();
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
