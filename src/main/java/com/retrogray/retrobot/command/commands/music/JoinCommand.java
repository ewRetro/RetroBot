package com.retrogray.retrobot.command.commands.music;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

@SuppressWarnings("ConstantConditions")
public class JoinCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(selfVoiceState.inVoiceChannel()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | Bot is already in another channel!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You need to be in a voice channel in order to execute this command!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();

        if (self.hasPermission(Permission.VOICE_CONNECT)) {
            audioManager.openAudioConnection(memberChannel);
            audioManager.setSelfDeafened(true);
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":loud_sound: | Connected to **"+ memberChannel.getName() + "**");
            channel.sendMessageEmbeds(embed.build()).queue();
        } else {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | Bot is missing permissions! (VOICE_CONNECT)")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
        }
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Join the voice channel of the user that ran the command!\n" +
                        "Usage: rb!join")
                .setColor(0xffcc00);
        return embed.build();
    }
    public List<String> getAliases() {
        return List.of("j", "connect");
    }
}
