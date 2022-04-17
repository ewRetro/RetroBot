package com.retrogray.retrobot.command.commands.fun;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HugCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        if (args.size() < 1 || message.getMentionedMembers().isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You must mention a user!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);


        if (target.equals(member)) {
            final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                    .setDescription(":heart: **" + target.getEffectiveName() + "** showed love to themselves.");
            channel.sendMessageEmbeds(embed3.build()).queue();
            return;
        }

        final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                .setDescription(":heart: **" + member.getEffectiveName() + "** hugged **" + target.getEffectiveName() + "**");
        channel.sendMessageEmbeds(embed2.build()).queue();
    }

    @Override
    public String getName() {
        return "hug";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
