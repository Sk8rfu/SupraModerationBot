package com.mikubot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.awt.*;

public class LogUtil {

    public static void log(Guild guild, EmbedBuilder embed) {

        TextChannel logChannel = guild.getTextChannelsByName("mod-logs", true)
                .stream().findFirst().orElse(null);

        if (logChannel == null) {
            logChannel = guild.createTextChannel("mod-logs").complete();
        }

        embed.setColor(Color.GRAY);
        embed.setFooter("Moderation Log");

        logChannel.sendMessageEmbeds(embed.build()).queue();
    }
}
