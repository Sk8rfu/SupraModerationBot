package com.mikubot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class LockCommand {

    public static void run(SlashCommandInteractionEvent event) {

        if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("❌ Нямаш право да заключваш канали.").setEphemeral(true).queue();
            return;
        }

        event.getChannel().asTextChannel().upsertPermissionOverride(
                event.getGuild().getPublicRole()
        ).deny(Permission.MESSAGE_SEND).queue();

        event.reply("🔒 Каналът е заключен.").queue();
    }
}
