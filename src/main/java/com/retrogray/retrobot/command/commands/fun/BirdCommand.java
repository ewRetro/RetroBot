package com.retrogray.retrobot.command.commands.fun;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class BirdCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://some-random-api.ml/animal/birb").async((json) -> {

            final String fact = json.get("fact").asText();
            final String url = json.get("image").asText();

            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setTitle("Random birb! :bird:")
                    .setFooter(fact)
                    .setImage(url);
            channel.sendMessageEmbeds(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "bird";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Get bird lmao!\n" +
                        "Usage: rb!bird [question]")
                .setColor(0xffcc00);
        return embed.build();
    }

    public List<String> getAliases() {
        return List.of("birb");
    }
}
