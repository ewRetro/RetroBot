package com.retrogray.retrobot.command.commands.fun;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.ContentType;
import me.duncte123.botcommons.web.WebParserUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.function.Consumer;

public class HasteCommand implements ICommand {

    private static final String HASTE_SERVER = "https://hasteb.in/";

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if (ctx.getArgs().isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Missing arguments!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final String invoke = this.getName();
        final String contentRaw = ctx.getMessage().getContentRaw();
        final int index = contentRaw.indexOf(invoke) + invoke.length();
        final String body = contentRaw.substring(index).trim();

        this.createPaste(body, (text) -> channel.sendMessage(text).queue());
    }

    private void createPaste(String text, Consumer<String> callback) {
        Request request = WebUtils.defaultRequest()
                .post(RequestBody.create(text.getBytes()))
                .addHeader("Content-Type", ContentType.TEXT_PLAIN.getType())
                .url(HASTE_SERVER + "documents")
                .build();

        WebUtils.ins.prepareRaw(request, (r) -> WebParserUtils.toJSONObject(r, new ObjectMapper())).async(
                (json) -> {
                    String key = json.get("key").asText();

                    callback.accept(HASTE_SERVER + key);
                },
                (e) -> callback.accept("Error: " + e.getMessage())
        );
    }

    @Override
    public String getName() {
        return "haste";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Post some text to hastebin\n" +
                        "Usage: rb!hastebin [text]")
                .setColor(0xffcc00);
        return embed.build();
    }
}