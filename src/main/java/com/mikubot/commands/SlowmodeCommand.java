package com.mikubot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlowmodeCommand {

    public static void run(SlashCommandInteractionEvent event) {

        int seconds = event.getOption("seconds").getAsInt();

        if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
            event.reply("❌ Нямаш право да променяш slowmode.").setEphemeral(true).queue();
            return;
        }

        event.getChannel().asTextChannel().getManager().setSlowmode(seconds).queue();

        event.reply("🐌 Slowmode е зададен на **" + seconds + " секунди**.").queue();
    }
}
