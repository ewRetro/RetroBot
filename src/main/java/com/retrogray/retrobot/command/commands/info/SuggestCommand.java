package com.retrogray.retrobot.command.commands.info;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SuggestCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        if (args.size() < 1) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Please provide a suggestion!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final String suggestion = String.join(" ", args.subList(0, args.size()));
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setTitle("New suggestion by "+ ctx.getAuthor().getAsTag())
                .setDescription("**Suggestion:** \n" + suggestion)
                .setFooter("Vote by reacting bellow!");
        channel.sendMessageEmbeds(embed.build()).queue(message1 -> {
            message1.addReaction("✅").queue();
            message1.addReaction("❌").queue();
        });
        message.delete().queue();
    }

    @Override
    public String getName() {
        return "suggest";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Suggest a feature for the bot!\n" +
                        "Usage: rb!suggest [suggestion]")
                .setColor(0xffcc00);
        return embed.build();
    }
}
