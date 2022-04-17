package com.retrogray.retrobot.command.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class EightBallCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();

        if (args.size() < 1) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Missing arguments!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        WebUtils.ins.getJSONObject("http://retrogray.com/rmapi").async((json) -> {

            final String answer = json.get("message").asText();

            final String question = String.join(" ", args.subList(0, args.size()));
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setTitle(":crystal_ball: The eight ball answered")
                    .setDescription("**Question:**\n" +
                            question + "\n" +
                            "**Answer:** \n" +
                            answer + "\n" +
                            "**Question asked by:** \n" +
                            member.getEffectiveName());
            channel.sendMessageEmbeds(embed2.build()).queue();
        });

    }

    @Override
    public String getName() {
        return "8ball";
    }


    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Ask the magical eight ball a question and get a response!\n" +
                        "Usage: rb!8ball [question]")
                .setColor(0xffcc00);
        return embed.build();
    }

    public List<String> getAliases() {
        return List.of("8b", "eb", "ball");
    }
}
