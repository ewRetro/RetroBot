package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.Config;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import com.retrogray.retrobot.lavaplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PlaylistCommand implements ICommand {

    private final HashMap<Long, Long> cooldown = new HashMap<>();

    @Override
    public void handle(CommandContext ctx) throws InterruptedException {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();
        String input = String.join("_", ctx.getArgs());
        final String pathName = Config.get("PLAYLIST_FOLDER_PATH") + input + ".txt";

        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
        TrackScheduler scheduler = musicManager.scheduler;

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();
        final VoiceChannel selfChannel = selfVoiceState.getChannel();
        PlayerManager manager = PlayerManager.getInstance();

        if (args.isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | Missing arguments!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        // Cooldown setup

        if (!cooldown.containsKey(member.getIdLong()) || System.currentTimeMillis() - cooldown.get(member.getIdLong()) > 10000) {
            cooldown.put(member.getIdLong(), System.currentTimeMillis());
            if (memberChannel == null) {
                final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | You must be in a voice channel for this to work!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed3.build()).queue();
                return;
            }

            try {
                if (!selfVoiceState.inVoiceChannel()) {

                    if (self.hasPermission(Permission.VOICE_CONNECT)) {
                        File file = new File(pathName);
                        Path filePath = file.toPath();
                        if (file.exists()) {
                            Scanner sc = new Scanner(file);
                            String song;
                            long lines;
                            lines = Files.lines(filePath).count();

                            audioManager.openAudioConnection(memberChannel);
                            self.getGuild().getAudioManager().setSelfDeafened(true);

                            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                                    .setTitle(":notes: | Local playlist loaded!")
                                    .setDescription("**Playlist FileName: **`" + input + "`\n" +
                                            "**Tracks loaded: **`" + lines + "`");
                            channel.sendMessageEmbeds(embed2.build()).queue();

                            while (sc.hasNextLine()) {
                                song = sc.nextLine();
                                if (!song.isEmpty() && !song.isBlank()) {
                                    manager.localPlaylistLoaded(channel, song);
                                }
                            }
                            sc.close();
                            return;
                        } else {
                            final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                                    .setDescription(":x: | Playlist does not exist!")
                                    .setColor(0xff0000);
                            channel.sendMessageEmbeds(embed3.build()).queue();
                            return;
                        }
                    } else {
                        final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                                .setDescription(":x: | Bot is missing permissions! (VOICE_CONNECT)")
                                .setColor(0xff0000);
                        channel.sendMessageEmbeds(embed3.build()).queue();
                        return;
                    }
                }
            } catch (NullPointerException | IOException e) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Something went wrong!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }

            if (Objects.equals(selfVoiceState.getChannel(), memberVoiceState.getChannel())) {
                File file = new File(pathName);
                Path filePath = file.toPath();
                if (file.exists()) {
                    Scanner sc = null;
                    try {
                        sc = new Scanner(file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    String song;
                    long lines = 0;

                    try {
                        lines = Files.lines(filePath).count();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                            .setTitle(":notes: | Local playlist loaded!")
                            .setDescription("**Playlist FileName: **`" + input + "`\n" +
                                    "**Tracks loaded: **`" + lines + "`");
                    channel.sendMessageEmbeds(embed2.build()).queue();

                    while (sc.hasNextLine()) {
                        song = sc.nextLine();

                        if (!song.isEmpty() && !song.isBlank()) {
                            manager.localPlaylistLoaded(channel, song);
                        }
                    }
                    sc.close();
                    scheduler.player.setPaused(false);
                    return;
                } else {
                    final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                            .setDescription(":x: | Playlist does not exist!")
                            .setColor(0xff0000);
                    channel.sendMessageEmbeds(embed3.build()).queue();
                    return;
                }
            } else {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Bot is playing in a different channel.")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }

        } else {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(10000 - (System.currentTimeMillis() - cooldown.get(member.getIdLong())));
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | This command is on cooldown for `" + seconds + "` seconds!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
        }

    }

    @Override
    public String getName() {
        return "playlist";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }

    @Override
    public List<String> getAliases() {
        return List.of("pl");
    }
}
