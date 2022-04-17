package com.retrogray.retrobot.command.commands.fun;

import com.retrogray.retrobot.command.CommandContext;
import com.retrogray.retrobot.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Random;

public class SlotMachineCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();



            Random r = new Random();
            int random = r.nextInt(12);

            if (random == 0) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":poop: :poop: :poop: \n" +
                                "**You won!**");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 1) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :crown: :crown: \n" +
                                "**You won!**");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 2) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":french_bread: :french_bread: :french_bread: \n" +
                                "**You won!**");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 3) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :french_bread: :poop: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 4) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :french_bread: :poop: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 5) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":poop: :french_bread: :poop: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 6) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :poop: :crown: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 7) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :poop: :poop: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 8) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":poop: :poop: :crown: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 9) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :poop: :french_bread: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 10) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":french_bread: :french_bread: :poop: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 11) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setTitle("Wrrr!")
                        .setDescription(":crown: :french_bread: :poop: \n" +
                                "**You lost!**")
                        .setColor(0xff0000);
                channel.sendMessageEmbeds(embed2.build()).queue();
            }
    }

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Try your luck on our slot machine!\n" +
                        "Usage: rb!slots")
                .setColor(0xffcc00);
        return embed.build();
    }
}
