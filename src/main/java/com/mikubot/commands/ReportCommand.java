package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class ReportCommand {

    public static void run(SlashCommandInteractionEvent event) {

        String message = event.getOption("message").getAsString();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("🚨 Нов репорт");
        embed.addField("От", event.getUser().getAsMention(), false);
        embed.addField("Съобщение", message, false);

        event.reply("📨 Репортът е изпратен към модераторите.").setEphemeral(true).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
