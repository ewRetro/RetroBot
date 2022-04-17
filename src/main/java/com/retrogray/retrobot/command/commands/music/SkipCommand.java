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

import java.util.List;

public class SkipCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;
        AudioPlayer player = musicManager.player;
        String nextSong = "";


        if (musicManager.scheduler.queue.peek() == null) {
            nextSong = "The queue is empty!";
        } else {
            nextSong = "**Up next:** `" + musicManager.scheduler.queue.peek().getInfo().title +"`";
        }



        if (player.getPlayingTrack() == null) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | No songs are playing!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();

            return;
        }

        scheduler.nextTrack();


        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle(":white_check_mark: | Skipping the current track!\n")
                .setDescription(nextSong);
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Skips the current track.\n" +
                        "Usage: rb!skip")
                .setColor(0xffcc00);
        return embed.build();
    }

    public List<String> getAliases() {
        return List.of("s", "next");
    }
}
