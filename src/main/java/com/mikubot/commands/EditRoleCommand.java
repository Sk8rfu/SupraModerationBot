package com.mikubot.commands;

import com.mikubot.utils.ColorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class EditRoleCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Role role = event.getOption("role").getAsRole();
        String newName = event.getOption("name") != null ? event.getOption("name").getAsString() : null;
        String colorInput = event.getOption("color") != null ? event.getOption("color").getAsString().toLowerCase() : null;

        var moderator = event.getMember();
        var bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямаш право да редактираш роли.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямам право да редактирам роли.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(role)) {
            event.reply("❌ Не можеш да редактираш роля по-висока от твоята.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(role)) {
            event.reply("❌ Не мога да редактирам роля по-висока от моята.").setEphemeral(true).queue();
            return;
        }

        // --- Проверка за съществуващо име ---
        if (newName != null) {
            Role existing = event.getGuild().getRolesByName(newName, true).stream().findFirst().orElse(null);

            if (existing != null && !existing.getId().equals(role.getId())) {
                event.reply("❌ Вече има роля с името **" + newName + "**.").setEphemeral(true).queue();
                return;
            }
        }

        // --- Парсване на цвят ---
        Color color = null;
        if (colorInput != null) {
            color = ColorUtils.parseColor(colorInput);
            if (color == null) {
                event.reply("❌ Невалиден цвят! Използвай RGB, HEX или име на цвят.").setEphemeral(true).queue();
                return;
            }
        }

        // --- Редактиране ---
        role.getManager()
                .setName(newName != null ? newName : role.getName())
                .setColor(color != null ? color : role.getColor())
                .queue();

        // --- Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(color != null ? color : role.getColor());
        embed.setTitle("🎨 Роля редактирана");
        embed.addField("Роля", role.getAsMention(), false);
        if (newName != null) embed.addField("Ново име", newName, false);
        if (colorInput != null) embed.addField("Нов цвят", colorInput, false);

        event.replyEmbeds(embed.build()).queue();
    }
}
