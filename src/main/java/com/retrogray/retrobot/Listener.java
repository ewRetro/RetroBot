package com.retrogray.retrobot;

import com.retrogray.retrobot.database.DatabaseManager;
import com.retrogray.retrobot.lavaplayer.GuildMusicManager;
import com.retrogray.retrobot.lavaplayer.PlayerManager;
import me.duncte123.botcommons.BotCommons;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        final AudioManager audioManager = event.getGuild().getAudioManager();
        PlayerManager playerManager = PlayerManager.getInstance();
        GuildMusicManager musicManager = playerManager.getGuildMusicManager(event.getGuild());
        final Member self = event.getGuild().getSelfMember();
        VoiceChannel connectedChannel = audioManager.getConnectedChannel();

        try {
            if (connectedChannel != null && connectedChannel.getMembers().contains(self)) {
                if ((long) connectedChannel.getMembers().size() == 1) {
                    musicManager.player.stopTrack();
                    musicManager.scheduler.getQueue().clear();
                    audioManager.closeAudioConnection();
                    musicManager.scheduler.player.setPaused(false);
                    musicManager.scheduler.repeating = false;
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }


        final long guildID = event.getGuild().getIdLong();
        String prefix = CstPrefix.PREFIXES.computeIfAbsent(guildID, DatabaseManager.INSTANCE::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("OWNER_ID"))) {
            LOGGER.info("Shutting down");
            event.getChannel().sendMessage("Bot is shutting down!").queue();
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        }

        if (raw.startsWith(prefix)) {
            try {
                manager.handle(event, prefix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        long guildID = 665577074878054401L;
        Member member = event.getMember();
        Guild g = event.getGuild();
        TextChannel channel = event.getGuild().getTextChannelById("944900941247832074");
        Role role = event.getGuild().getRolesByName("Member", false).get(0);
        if (event.getGuild().getIdLong() == guildID) {
            g.addRoleToMember(member, role).queue();
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription("Welcome "+member.getAsMention()+" to the server!\n" +
                            "Roles added: "+ role.getAsMention());
            assert channel != null;
            channel.sendMessageEmbeds(embed.build()).queue();
        } else {
            return;
        }
    }
}

