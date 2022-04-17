package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class QueueCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        Queue<AudioTrack> queue = musicManager.scheduler.getQueue();

        if (queue.isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription("The queue is empty!");
            channel.sendMessageEmbeds(embed.build()).queue();

            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> tracks = new ArrayList<>(queue);
        EmbedBuilder builder = EmbedUtils.defaultEmbed()
                .setTitle("Current Queue (Total: " + queue.size() + ")");

        for (int i = 0; i < trackCount; i++) {
            AudioTrack track = tracks.get(i);
            AudioTrackInfo info = track.getInfo();

            builder.appendDescription(String.format(
                    "%s - %s\n",
                    info.title,
                    info.author
            ));
        }

        channel.sendMessageEmbeds(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("See the queued up songs.\n" +
                        "Usage: rb!queue")
                .setColor(0xffcc00);
        return embed.build();
    }

    public List<String> getAliases() {
        return List.of("q");
    }
}
