package com.retrogray.retrobot.command.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URL;

public class DogCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://dog.ceo/api/breeds/image/random").async((json) -> {


            final String url = json.get("message").asText();

            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setTitle("Random Doggo! :dog:")
                    .setImage(url);
            channel.sendMessageEmbeds(embed.build()).queue();
        });

    }

    @Override
    public String getName() {
        return "dog";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
