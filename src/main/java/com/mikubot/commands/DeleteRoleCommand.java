package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class DeleteRoleCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Role role = event.getOption("role").getAsRole();
        var moderator = event.getMember();
        var bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (role.isPublicRole()) {
            event.reply("❌ Не можеш да изтриеш ролята @everyone.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямаш право да триеш роли.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямам право да трия роли.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(role)) {
            event.reply("❌ Не можеш да изтриеш роля по-висока от твоята.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(role)) {
            event.reply("❌ Не мога да изтрия роля по-висока от моята.").setEphemeral(true).queue();
            return;
        }

        // --- Подготвяме embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("🗑 Роля изтрита");
        embed.addField("Роля", role.getName(), false);

        // --- Правилен ред: първо отговаряме, после трием ---
        event.replyEmbeds(embed.build()).queue(success -> {
            role.delete().queue();
        });
    }
}
