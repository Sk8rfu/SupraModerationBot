package com.mikubot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class NicknameCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        Member target = event.getOption("user").getAsMember();

        // name може да е null!
        String name = event.getOption("name") != null
                ? event.getOption("name").getAsString().trim()
                : null;

        // reset може да е null!
        boolean reset = event.getOption("reset") != null
                && event.getOption("reset").getAsBoolean();

        // Проверка за права на модератора
        if (!moderator.hasPermission(Permission.NICKNAME_MANAGE)) {
            event.reply("❌ Нямаш право да променяш прякори.").setEphemeral(true).queue();
            return;
        }

        // Проверка дали ботът има право
        if (!bot.hasPermission(Permission.NICKNAME_MANAGE)) {
            event.reply("❌ Нямам право да променям прякори.").setEphemeral(true).queue();
            return;
        }

        // Проверка дали ботът може да интерактва с target
        if (!bot.canInteract(target)) {
            event.reply("❌ Не мога да променя прякора на този потребител (ролята му е по-висока от моята).")
                    .setEphemeral(true).queue();
            return;
        }

        // Проверка дали модераторът може да интерактва с target
        if (!moderator.canInteract(target)) {
            event.reply("❌ Не можеш да променяш прякора на потребител с по-висока роля от твоята.")
                    .setEphemeral(true).queue();
            return;
        }

        // --- RESET ---
        if (reset) {

            if (target.getNickname() == null) {
                event.reply("ℹ " + target.getAsMention() + " няма прякор, който да бъде ресетнат.")
                        .setEphemeral(true).queue();
                return;
            }

            target.modifyNickname(null).queue();
            event.reply("♻ Прякорът на " + target.getAsMention() + " беше върнат към оригиналното му име.").queue();
            return;
        }

        // --- Ако няма reset и няма име ---
        if (name == null || name.isEmpty()) {
            event.reply("❌ Трябва да въведеш нов прякор или да използваш reset.")
                    .setEphemeral(true).queue();
            return;
        }

        // --- Промяна на прякор ---
        target.modifyNickname(name).queue();
        event.reply("✏ Прякорът на " + target.getAsMention() + " е променен на **" + name + "**.").queue();
    }
}
