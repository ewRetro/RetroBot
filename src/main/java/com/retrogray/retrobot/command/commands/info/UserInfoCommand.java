package com.retrogray.retrobot.command.commands.info;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final List<String> args = ctx.getArgs();

        Member memb;

        if (!args.isEmpty()) {
            memb = message.getMentionedMembers().get(0);
        } else {
            memb = ctx.getMember();
        }

        String NAME = memb.getEffectiveName();
        String TAG = memb.getUser().getName() + "#" + memb.getUser().getDiscriminator();
        String GUILD_JOIN_DATE = memb.getTimeJoined().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String DISCORD_JOINED_DATE = memb.getUser().getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME);
        String ID = memb.getUser().getId();
        String ROLES = "";
        String AVATAR = memb.getUser().getAvatarUrl();


        for ( Role r : memb.getRoles() ) {
            ROLES += r.getAsMention() + ", ";
        }
        if (ROLES.length() > 0)
            ROLES = ROLES.substring(0, ROLES.length()-2);
        else
            ROLES = "No roles on this server.";

        if (AVATAR == null) {
            AVATAR = "No Avatar";
        }


        EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription(":spy:   **User information for " + memb.getUser().getName() + ":**")
                .addField("Name / Nickname", NAME, false)
                .addField("User Tag", TAG, false)
                .addField("ID", ID, false)
                .addField("Roles", ROLES, false)
                .addField("Guild Joined", GUILD_JOIN_DATE, false)
                .addField("Account Created", DISCORD_JOINED_DATE, false)
                .addField("Avatar-URL", AVATAR, false);
                if (AVATAR != "No Avatar") {
                    embed.setThumbnail(AVATAR);
                }

        ctx.getChannel().sendMessageEmbeds(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
