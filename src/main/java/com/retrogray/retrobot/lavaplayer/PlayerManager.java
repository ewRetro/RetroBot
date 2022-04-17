package com.retrogray.retrobot.lavaplayer;

import com.github.topislavalinkplugins.spotify.SpotifyConfig;
import com.github.topislavalinkplugins.spotify.SpotifySourceManager;
import com.retrogray.retrobot.Config;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeHttpContextFilter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PlayerManager {
    private static PlayerManager INSTANCE;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    //Player Manager setup
    //Source managers setup
    //Spotify setup
    private PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        SpotifyConfig spotifyConfig = new SpotifyConfig();
        spotifyConfig.setClientId(Config.get("SPOTIFY_CLIENT_ID"));
        spotifyConfig.setClientSecret(Config.get("SPOTIFY_CLIENT_SECRET"));
        spotifyConfig.setCountryCode("US");
        playerManager.registerSourceManager(new SpotifySourceManager(spotifyConfig, playerManager));
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        YoutubeHttpContextFilter.setPAPISID(Config.get("PAPISID"));
        YoutubeHttpContextFilter.setPSID(Config.get("PSID"));

    }


    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.playerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }


    public synchronized GuildMusicManager getGuildMusicManager(Guild guild) {
        long guildId = guild.getIdLong();
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }




    public void loadAndPlay(TextChannel channel, String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());


        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {

                play(musicManager, track);
                String queueSize = String.valueOf(musicManager.scheduler.queue.size());

                if (musicManager.scheduler.queue.isEmpty()) {
                    queueSize = "**as the first track!**";
                } else {
                    queueSize = "**at position** `(#"+queueSize+")`";
                }
                AudioTrackInfo info = track.getInfo();
                final String nowPlaying = String.format("**Adding to queue:** [%s](%s) %s",
                        info.title,
                        info.uri,
                        queueSize);
                String thumbnail = "http://img.youtube.com/vi/"+info.identifier+"/mqdefault.jpg";



                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle("<a:greenDisc:963914601639055420> | Queue updated!")
                        .setThumbnail(thumbnail)
                        .setDescription(nowPlaying);
                channel.sendMessageEmbeds(embed.build()).queue();

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {


                if (playlist.isSearchResult()) {
                    AudioTrack firstTrack = playlist.getSelectedTrack();
                    if (firstTrack == null) {
                        firstTrack = playlist.getTracks().remove(0);
                    }

                    play(musicManager, firstTrack);
                    String queueSize = String.valueOf(musicManager.scheduler.queue.size());

                    if (musicManager.scheduler.queue.isEmpty()) {
                        queueSize = "**as the first track!**";
                    } else {
                        queueSize = "**at position** `(#"+queueSize+")`";
                    }
                    AudioTrackInfo info = firstTrack.getInfo();
                    final String nowPlaying = String.format("**Adding to queue:** [%s](%s) %s",
                            info.title,
                            info.uri,
                            queueSize);

                    String thumbnail = "http://img.youtube.com/vi/"+info.identifier+"/mqdefault.jpg";



                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setTitle("<a:greenDisc:963914601639055420> | Queue updated!")
                            .setThumbnail(thumbnail)
                            .setDescription(nowPlaying);
                    channel.sendMessageEmbeds(embed.build()).queue();

                } else {
                    for (AudioTrack track : playlist.getTracks()) {
                        play(musicManager, track);
                    }

                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setTitle("<a:greenDisc:963914601639055420> | Queue updated!")
                            .setDescription("Adding playlist to queue: **"+playlist.getName()+"**, with `"+playlist.getTracks().size()+"` tracks.");
                    channel.sendMessageEmbeds(embed.build()).queue();
                }

            }


            @Override
            public void noMatches() {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Nothing found for: "+trackUrl)
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Could not play "+ exception.getMessage())
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();

            }

            public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Could not play ")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }


        });

    }

    private void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    public void localPlaylistLoaded(TextChannel channel, String trackUrl) {
        GuildMusicManager musicManager = getGuildMusicManager(channel.getGuild());


        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack track) {
                try {
                    play(musicManager, track);
                    TimeUnit.SECONDS.sleep(7);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                return;
            }

            @Override
            public void noMatches() {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Nothing found for: "+trackUrl)
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Could not play "+ exception.getMessage())
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        });
    }





    public static synchronized PlayerManager getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }
}