package com.retrogray.retrobot.command.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class JokeCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async((json) -> {
            if(!json.get("success").asBoolean()) {
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setDescription(":x: Something went wrong, please try again later")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed.build()).queue();
                System.out.println(json);
                return;
            }

            final JsonNode data = json.get("data");
            final String title = data.get("title").asText();
            final String url = data.get("url").asText();
            final String body = data.get("body").asText();
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setTitle(title, url)
                    .setDescription(body);

            channel.sendMessageEmbeds(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "joke";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Show a random joke!\n" +
                        "Usage: rb!Joke")
                .setColor(0xffcc00);
        return embed.build();
    }
}
