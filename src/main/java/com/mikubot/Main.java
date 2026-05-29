package com.mikubot;

import com.mikubot.config.Config;
import com.mikubot.listeners.EventListener;
import com.mikubot.CommandManager;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    public static void main(String[] args) {

        JDABuilder builder = JDABuilder.createDefault(Config.TOKEN)
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.GUILD_VOICE_STATES
                );

        builder.addEventListeners(new EventListener());
        builder.addEventListeners(new CommandManager());

        builder.build();
    }
}
