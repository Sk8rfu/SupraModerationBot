package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class PingCommand {

    public static void run(SlashCommandInteractionEvent event) {

        long gatewayPing = event.getJDA().getGatewayPing();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🏓 Pong!");
        embed.setColor(Color.GREEN);
        embed.addField("Gateway Ping", gatewayPing + "ms", true);

        embed.setFooter("SupraModerationBot • Ping информация", event.getJDA().getSelfUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
    }
}
