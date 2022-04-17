package com.retrogray.retrobot.command.commands.fun;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.util.List;
import java.util.Random;

public class SusCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) throws InterruptedException {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final Member self = ctx.getSelfMember();
        if(args.size() < 1) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: | You must mention a user!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);
        String [] susOptions = {
                        ". 　　　。　　　　•　 　ﾟ　　。 　　.\n" +
                        "　　　.　　　 　　.　　　　　。　　 。　. 　\n" +
                        ".　　 。　　　　　  。 . 　　 • 　　　　•\n" +
                        "　　ﾟ　　 " + target.getAsMention() + " was The Impostor.　 。　.\n" +
                        "　　'　　　 No Impostor remains 　 　　。\n" +
                        "　　ﾟ　　　.　　　. ,　　　　.　 .",

                        ". 　　　。　　　　•　 　ﾟ　　。 　　.\n" +
                        "　　　.　　　 　　.　　　　　。　　 。　. 　\n" +
                        ".　　 。　　　　　  。 . 　　 • 　　　　•\n" +
                        "　　ﾟ　　 " + target.getAsMention() + " was not The Impostor.　 。　.\n" +
                        "　　'　　　 1 Impostor remains 　 　　。\n" +
                        "　　ﾟ　　　.　　　. ,　　　　.　 ."

        };
        Random random = new Random();
        int select = random.nextInt(susOptions.length);
        channel.sendMessage(susOptions[select]).queue();

    }

    @Override
    public String getName() {
        return "sus";
    }

    @Override
    public MessageEmbed getHelp() {
        return null;
    }
}
