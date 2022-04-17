package com.retrogray.retrobot.command.commands.fun;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class CatCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
    //https://aws.random.cat/meow
        TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://aws.random.cat/meow").async((json) -> {


            final String url = json.get("file").asText();

            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setTitle("Random Cat! :cat:")
                    .setImage(url);
            channel.sendMessageEmbeds(embed.build()).queue();
        });
    }

    @Override
    public String getName() {
        return "cat";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
