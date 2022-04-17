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

import java.util.List;
import java.util.Random;

public class YesNoCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();

        if (args.size() < 1) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Missing arguments!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }



        Random r = new Random();
        int random = r.nextInt(6);

        final String question = String.join(" ", args.subList(0, args.size()));
        WebUtils.ins.getJSONObject("https://yesno.wtf/api").async((json) -> {
            final String title = json.get("answer").asText().toUpperCase();
            final String image = json.get("image").asText();
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setTitle(title)
                    .setDescription("**Question:** \n" + question)
                    .setImage(image);


            channel.sendMessageEmbeds(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "yesno";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Yes or no?!\n" +
                        "Usage: rb!yesno [question]")
                .setColor(0xffcc00);
        return embed.build();
    }
}
