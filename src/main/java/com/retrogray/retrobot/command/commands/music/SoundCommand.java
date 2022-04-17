package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.Config;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import com.retrogray.retrobot.lavaplayer.TrackScheduler;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class SoundCommand implements ICommand {

    private final HashMap<Long, Long> cooldown = new HashMap<>();

    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (args.isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | Missing arguments!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();

            return;
        }

        // Cooldown on the command.

        if (!cooldown.containsKey(member.getIdLong()) || System.currentTimeMillis() - cooldown.get(member.getIdLong()) > 10000) {
            cooldown.put(member.getIdLong(), System.currentTimeMillis());
            String input = String.join("_", ctx.getArgs());
            final String pathName = Config.get("SOUNDS_FOLDER_PATH") + input + ".mp3";

            PlayerManager playerManager = PlayerManager.getInstance();
            GuildMusicManager musicManager = playerManager.getGuildMusicManager(ctx.getGuild());
            TrackScheduler scheduler = musicManager.scheduler;


            final GuildVoiceState memberVoiceState = ctx.getMember().getVoiceState();


            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            final VoiceChannel selfChannel = selfVoiceState.getChannel();
            PlayerManager manager = PlayerManager.getInstance();

            if (memberChannel == null) {
                final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | You must be in a voice channel for this to work!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed3.build()).queue();
                return;
            }

            /*
             * Try Catch statement:
             * Check if bot NOT is in voice channel
             * Check if bot has connection permission
             * Connect to Voice Channel
             * Set self deafen to true
             * Play requested song
             */

            try {
                if (!selfVoiceState.inVoiceChannel()) {

                    if (self.hasPermission(Permission.VOICE_CONNECT)) {

                        audioManager.openAudioConnection(memberChannel);
                        self.getGuild().getAudioManager().setSelfDeafened(true);
                        manager.loadAndPlay(ctx.getChannel(), pathName);
                        final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                                .setDescription(":loud_sound: | Connected to **" + memberChannel.getName() + "**. Soundboard loading...");
                        channel.sendMessageEmbeds(embed2.build()).queue();
                        return;

                    } else {
                        final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                                .setDescription(":x: | Bot is missing permissions! (VOICE_CONNECT)")
                                .setColor(0xff0000);
                        channel.sendMessageEmbeds(embed3.build()).queue();
                    }
                }
            } catch (NullPointerException e) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | I don't feel like playing.")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }

            /*
             * If Bot voice sate is the same as Member Voice state:
             * Load and play requested track
             * Un-pause player.
             */

            if (Objects.equals(selfVoiceState.getChannel(), memberVoiceState.getChannel())) {
                manager.loadAndPlay(ctx.getChannel(), pathName);
                scheduler.player.setPaused(false);
            } else {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Bot is playing in a different channel.")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
            }

        } else {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(10000 - (System.currentTimeMillis() - cooldown.get(member.getIdLong())));
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | This command is on cooldown for `"+ seconds +"` seconds!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
        return;


    }

    @Override
    public String getName() {
        return "sound";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
