package com.retrogray.retrobot.command.commands.admin;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.stream.Collectors;

public class UnbanCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if(args.isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You must mention a user!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        } else {
            String strmember = String.join(" ", args);
            Member moderator = ctx.getMember();
            if(!moderator.hasPermission(Permission.BAN_MEMBERS)) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | You don't have permission to Unban members!")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            if(!ctx.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: | Bot is missing permissions! (`BAN_MEMBERS`)")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                return;
            }
            //Checks through the ban list and finds possible targets given out arguments of the command and bans the first possible target in our list
            //Our arguments now don't only have to be userID but now they can be userTag or userName
            ctx.getGuild().retrieveBanList().queue((bans) -> {
                //Finds all possible targets
                List<User> possibleTargets = bans.stream().filter((ban) -> isRightUser(ban, strmember)).map(Guild.Ban::getUser).collect(Collectors.toList());
                if (possibleTargets.isEmpty()) {
                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setDescription(":x: | This user is not banned!")
                            .setColor(0xff0000);
                    channel.sendMessageEmbeds(embed.build()).queue();
                    return;
                }
                //Unbans the first possible target found!
                User target = possibleTargets.get(0);
                String targetName = String.format("%#s", target);
                ctx.getGuild().unban(target).queue();
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":white_check_mark: | User `"+targetName+"` is now unbanned!");
                channel.sendMessageEmbeds(embed.build()).queue();
            });
        }
    }

    @Override
    public String getName() {
        return "unban";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }

    private static boolean isRightUser(Guild.Ban ban, String args) {
        User bannedUser = ban.getUser();
        return bannedUser.getName().equalsIgnoreCase(args) || bannedUser.getId().equals(args)
                || bannedUser.getAsTag().equalsIgnoreCase(args);
    }
}
