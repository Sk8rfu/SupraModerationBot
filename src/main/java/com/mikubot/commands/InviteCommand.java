package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class InviteCommand {

    public static void run(SlashCommandInteractionEvent event) {

        String expiresRaw = event.getOption("expires").getAsString(); // 1s, 5m, 2h, 1w
        int uses = event.getOption("uses").getAsInt();

        TextChannel channel = event.getChannel().asTextChannel();

        // --- Конвертиране ---
        int expires = parseTime(expiresRaw);

        if (expires < 0) {
            event.reply("❌ Невалиден формат! Използвай: `1s`, `1m`, `1h`, `1w`").setEphemeral(true).queue();
            return;
        }

        channel.createInvite()
                .setMaxAge(expires)   // секунди
                .setMaxUses(uses)     // брой използвания
                .queue(invite -> {

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.GREEN);
                    embed.setTitle("🔗 Създадена е покана");
                    embed.addField("Покана", invite.getUrl(), false);
                    embed.addField("Валидност", formatTime(expiresRaw), false);
                    embed.addField("Използвания", uses == 0 ? "Без лимит" : String.valueOf(uses), false);

                    event.replyEmbeds(embed.build()).queue();
                });
    }

    // --- Преобразуване на 1s / 1m / 1h / 1w в секунди ---
    private static int parseTime(String input) {
        try {
            input = input.toLowerCase();

            if (input.endsWith("s")) {
                return Integer.parseInt(input.replace("s", ""));
            }
            if (input.endsWith("m")) {
                return Integer.parseInt(input.replace("m", "")) * 60;
            }
            if (input.endsWith("h")) {
                return Integer.parseInt(input.replace("h", "")) * 3600;
            }
            if (input.endsWith("w")) {
                return Integer.parseInt(input.replace("w", "")) * 604800;
            }

            return -1; // невалиден формат

        } catch (Exception e) {
            return -1;
        }
    }

    // --- Красив текст за embed ---
    private static String formatTime(String input) {
        input = input.toLowerCase();

        if (input.endsWith("s")) return input.replace("s", "") + " секунди";
        if (input.endsWith("m")) return input.replace("m", "") + " минути";
        if (input.endsWith("h")) return input.replace("h", "") + " часа";
        if (input.endsWith("d")) return input.replace("d", "") + " дни";
        if (input.endsWith("w")) return input.replace("w", "") + " седмици";

        return "Неизвестно";
    }
}
