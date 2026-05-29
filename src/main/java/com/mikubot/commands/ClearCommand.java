package com.mikubot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ClearCommand {

    public static void run(SlashCommandInteractionEvent event) {

        int amount = event.getOption("amount").getAsInt();

        if (!event.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            event.reply("❌ Нямаш право да триеш съобщения.").setEphemeral(true).queue();
            return;
        }

        if (amount < 1) {
            event.reply("❌ Трябва да изтриеш **минимум 1** съобщение.").setEphemeral(true).queue();
            return;
        }

        if (amount > 100) {
            event.reply("❌ Можеш да триеш **максимум 100** съобщения наведнъж.").setEphemeral(true).queue();
            return;
        }

        event.getChannel().asTextChannel().getHistory().retrievePast(amount).queue(messages -> {

            if (messages.isEmpty()) {
                event.reply("❌ Няма съобщения за изтриване.").setEphemeral(true).queue();
                return;
            }

            if (messages.size() == 1) {
                // Ако има само 1 съобщение → трие се индивидуално
                event.getChannel().asTextChannel().deleteMessageById(messages.get(0).getId()).queue();
                event.reply("🧹 Изтрито е **1** съобщение.").setEphemeral(true).queue();
                return;
            }

            // Ако има 2–100 съобщения → bulk delete
            event.getChannel().asTextChannel().deleteMessages(messages).queue(
                    success -> event.reply("🧹 Изтрити са **" + messages.size() + "** съобщения.").setEphemeral(true).queue(),
                    error -> event.reply("❌ Не мога да изтрия някои съобщения (вероятно са по-стари от 14 дни).").setEphemeral(true).queue()
            );
        });
    }
}
