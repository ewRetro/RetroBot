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

public class KillCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();

        if (args.size() < 1 || message.getMentionedMembers().isEmpty()) {
            final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                    .setDescription(":x: You must mention a user!")
                    .setColor(0xff0000);
            channel.sendMessageEmbeds(embed.build()).queue();
            return;
        }

        final Member target = message.getMentionedMembers().get(0);


        if (target.equals(member)) {
            final EmbedBuilder embed3 = EmbedUtils.defaultEmbed()
                    .setDescription(":gun: **" + target.getEffectiveName() + "** took the easy way out.");
            channel.sendMessageEmbeds(embed3.build()).queue();
            return;
        }

        if (target.equals(ctx.getSelfMember())){

            Random r = new Random();
            int random = r.nextInt(6);

            if (random == 0) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setDescription("You know you can't kill me.");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 1) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setDescription("If you kill me I can't kill others, so pleas don't!");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 2) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setDescription("**insert matrix dodging meme here**");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 3) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setDescription("Reflects bullet! \n" +
                                "**"+ctx.getSelfMember().getEffectiveName()+"** " + "killed" + " **"+member.getEffectiveName()+"**");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 4) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setDescription("You can't get through my virtual shield!");
                channel.sendMessageEmbeds(embed2.build()).queue();
            } else if (random == 5) {
                final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                        .setDescription("I just can't be stopped!");
                channel.sendMessageEmbeds(embed2.build()).queue();
            }
            return;
        }

        Random r = new Random();
        int random = r.nextInt(6);

        if (random == 0) {
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setDescription(":gun: **" + member.getEffectiveName() + "** killed **" + target.getEffectiveName() + "**");
            channel.sendMessageEmbeds(embed2.build()).queue();
        } else if (random == 1) {
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setDescription("**" + member.getEffectiveName() + "** killed **" + target.getEffectiveName() + "** with an :apple:");
            channel.sendMessageEmbeds(embed2.build()).queue();
        } else if (random == 2) {
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setDescription("**" + target.getEffectiveName() + "** got killed by **" + member.getEffectiveName() + "** by using water powers.");
            channel.sendMessageEmbeds(embed2.build()).queue();
        } else if (random == 3) {
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setDescription("**" + member.getEffectiveName() + "** killed **" + target.getEffectiveName() + "** using a **Chainsaw**");
            channel.sendMessageEmbeds(embed2.build()).queue();
        } else if (random == 4) {
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setDescription("**" + member.getEffectiveName() + "** killed **" + target.getEffectiveName() + "** using a **TNT**");
            channel.sendMessageEmbeds(embed2.build()).queue();
        } else if (random == 5) {
            final EmbedBuilder embed2 = EmbedUtils.defaultEmbed()
                    .setDescription("**" + member.getEffectiveName() + "** hit **" + target.getEffectiveName() + "** into the void");
            channel.sendMessageEmbeds(embed2.build()).queue();
        }


    }

    @Override
    public String getName() {
        return "kill";
    }

    @Override
    public MessageEmbed getHelp() {
        final EmbedBuilder embed = EmbedUtils.defaultEmbed()
                .setDescription("Kills the user you mention!!\n" +
                        "Usage: rb!kill <user>")
                .setColor(0xffcc00);
        return embed.build();
    }
}
