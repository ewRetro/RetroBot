package com.retrogray.retrobot.command.commands.info;

import com.github.natanbc.reliqua.util.StatusCodeValidator;
import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();

        if (args.size() < 2) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: Correct usage is `<prefix>minecraft uuid/names/skin <useranme/uuid>`")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final String item = args.get(0).toLowerCase();
        final String id = args.get(1);

        if(item.equals("uuid")) {
            fetchUUID(id, (uuid) -> {
                if (uuid == null) {
                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setDescription(":x: User with name `"+id+"` does not exist!")
                            .setColor(0xff0000);
                    channel.sendMessageEmbeds(embed.build()).queue();
                    return;
                }

                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle("UUID Check")
                        .setDescription("**User requested:** `"+ id + "`\n" +
                                "**UUID**: `" + uuid + "`");
                channel.sendMessageEmbeds(embed.build()).queue();
            });
        } else if (item.equals("names")) {
            fetchNameHistory(id, (names) -> {
                if (names == null) {
                    final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                            .setDescription(":x: That UUDI is not valid!")
                            .setColor(0xff0000);
                    channel.sendMessageEmbeds(embed.build()).queue();
                    return;
                }

                final String namesJoined = String.join(", ", names);

                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle("Name History Check")
                        .setDescription("**UUID searched:** `" + id + "`\n" +
                                "**Names:**: `" + namesJoined + "`");
                channel.sendMessageEmbeds(embed.build()).queue();
            });
        } else if(item.equals("skin")) {


            try {
                URL imageUrl = new URL("https://minecraftskinstealer.com/api/v1/skin/render/fullbody/"+ id +"/700");
                final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                        .setTitle("Skin of player "+ id)
                        .setImage(String.valueOf(imageUrl))
                                .setFooter("Note: If a skin does not exist the bot will display a cat skin with black pixels.");
                channel.sendMessageEmbeds(embed.build()).queue();
            }
            catch (IOException ioe) {
                //log the error
            }
        } else {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: `"+item+"` is not known, please either choose **uuid** or **names**!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
        }

    }

    @Override
    public String getName() {
        return "minecraft";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }

    private void fetchUUID(String username, Consumer<String> callback) {
        WebUtils.ins.getJSONObject(
          "https://api.mojang.com/users/profiles/minecraft/" + username,
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json) -> {
                    callback.accept(json.get("id").asText());
                },
                (error) -> {
                    callback.accept(null);
                }
        );
    }

    private void fetchNameHistory(String uuid, Consumer<List<String>> callback) {
        WebUtils.ins.getJSONArray(
                "https://api.mojang.com/user/profiles/" + uuid + "/names",
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                (json) -> {
                    List<String> names = new ArrayList<>();
                    json.forEach((item) -> names.add(item.get("name").asText()));

                    callback.accept(names);
                },
                (error) -> {
                    callback.accept(null);
                }
        );
    }

}
