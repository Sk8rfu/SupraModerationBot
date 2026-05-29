package com.mikubot.commands;

import com.mikubot.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class CreateRoleCommand {

    public static void run(SlashCommandInteractionEvent event) {

        String roleName = event.getOption("name").getAsString();
        String colorInput = event.getOption("color").getAsString().toLowerCase();

        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямаш право да създаваш роли.").setEphemeral(true).queue();
            return;
        }

        // --- Проверка дали роля със същото име вече съществува ---
        Role existing = event.getGuild().getRolesByName(roleName, true).stream().findFirst().orElse(null);
        if (existing != null) {
            event.reply("❌ Роля с името **" + roleName + "** вече съществува!").setEphemeral(true).queue();
            return;
        }

        // --- Опит за парсване на цвета ---
        Color color = ColorUtils.parseColor(colorInput);

        if (color == null) {
            event.reply("❌ Невалиден цвят! Използвай:\n" +
                    "• RGB: `255 0 128`\n" +
                    "• Hex: `#ff00aa`\n" +
                    "• Име: `red`, `blue`, `green`, `yellow` ...").setEphemeral(true).queue();
            return;
        }

        // --- Създаване на роля ---
        event.getGuild().createRole()
                .setName(roleName)
                .setColor(color)
                .queue(role -> {

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(color);
                    embed.setTitle("🎨 Роля създадена");
                    embed.addField("Име", role.getAsMention(), false);
                    embed.addField("Цвят", colorInput, false);

                    event.replyEmbeds(embed.build()).queue();
                });
    }
}
