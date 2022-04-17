package com.retrogray.retrobot;

import com.retrogray.retrobot.database.SQLiteDataSource;
import com.retrogray.retrobot.utils.SQLiteReconnect;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Bot {



    private Bot() throws LoginException {

        WebUtils.setUserAgent("Opera/1.0 RetroBot / retrogray.com");
        EmbedUtils.setEmbedBuilder(() -> new EmbedBuilder().setColor(0x00a550));

        JDABuilder.createDefault(
                Config.get("TOKEN"), GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .disableCache(EnumSet.of(CacheFlag.EMOTE))
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.playing("with the guild"))
                .addEventListeners(new Listener()).build();
    }



    public static void main(String[] args) throws LoginException {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        new Bot();
    }



}
