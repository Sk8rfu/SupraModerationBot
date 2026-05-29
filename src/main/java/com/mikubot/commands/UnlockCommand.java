package com.mikubot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class UnlockCommand {

    public static void run(SlashCommandInteractionEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("❌ Нямаш право да отключваш канали.").setEphemeral(true).queue();
            return;
        }

        var channel = event.getChannel().asTextChannel();
        var publicRole = event.getGuild().getPublicRole();

        PermissionOverride override = channel.getPermissionOverride(publicRole);

        if (override != null) {
            override.delete().queue(
                    success -> event.reply("🔓 Каналът е отключен.").queue(),
                    error -> event.reply("❌ Не мога да отключа канала (нямам достъп до override-а).").setEphemeral(true).queue()
            );
            return;
        }

        event.reply("🔓 Каналът вече е отключен.").queue();
    }
}
