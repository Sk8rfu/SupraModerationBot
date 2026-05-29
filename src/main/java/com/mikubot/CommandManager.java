package com.mikubot;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {

        event.getJDA().updateCommands().addCommands(

                // 📘 Информация
                Commands.slash("ping", "Показва latency"),
                Commands.slash("help", "Показва всички команди"),
                Commands.slash("userinfo", "Информация за потребител")
                        .addOption(OptionType.USER, "user", "Потребител", false),
                Commands.slash("serverinfo", "Информация за сървъра"),
                Commands.slash("avatar", "Показва аватар")
                        .addOption(OptionType.USER, "user", "Потребител", false),
                Commands.slash("about", "Информация за бота"),

                // 🛡 Модерация
                Commands.slash("ban", "Банва потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.STRING, "reason", "Причина", false),

                Commands.slash("tempban", "Временен бан на потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.STRING, "time", "Време (1s, 1m, 1h, 1d, 1w)", true)
                        .addOption(OptionType.STRING, "reason", "Причина", false),

                Commands.slash("unban", "Премахва бан")
                        .addOption(OptionType.STRING, "userid", "ID на потребителя или името", true),

                Commands.slash("banid", "Банва потребител по ID")
                        .addOption(OptionType.STRING, "userid", "ID на потребителя", true)
                        .addOption(OptionType.STRING, "reason", "Причина", false),

                Commands.slash("kick", "Киква потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.STRING, "reason", "Причина", false),

                Commands.slash("mute", "Мютва потребител (timeout)")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.INTEGER, "minutes", "Продължителност в минути", false)
                        .addOption(OptionType.STRING, "reason", "Причина за mute", false),

                Commands.slash("banlist", "Показва списък с баннати потребители"),

                Commands.slash("unmute", "Размютва потребител (timeout)")
                        .addOption(OptionType.USER, "user", "Потребител", true),

                Commands.slash("muterole", "Мютва потребител чрез роля")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.STRING, "reason", "Причина за mute", false),

                Commands.slash("unmuterole", "Размютва потребител чрез роля")
                        .addOption(OptionType.USER, "user", "Потребител", true),

                Commands.slash("warn", "Предупреждава потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.STRING, "reason", "Причина", false),

                Commands.slash("unwarn", "Премахва всички предупреждения на потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true),

                Commands.slash("warnings", "Показва предупрежденията на потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true),

                // ⚙ Управление
                Commands.slash("giverole", "Дава роля на потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.ROLE, "role", "Роля", true),

                Commands.slash("removerole", "Маха роля от потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.ROLE, "role", "Роля", true),

                Commands.slash("createrole", "Създава роля")
                        .addOption(OptionType.STRING, "name", "Име на ролята", true)
                        .addOption(OptionType.STRING, "color", "Цвят (RGB, HEX или име)", true),

                Commands.slash("editrole", "Редактира роля")
                        .addOption(OptionType.ROLE, "role", "Роля", true)
                        .addOption(OptionType.STRING, "name", "Ново име", false)
                        .addOption(OptionType.STRING, "color", "Нов цвят (RGB, HEX или име)", false),

                Commands.slash("deleterole", "Изтрива роля")
                        .addOption(OptionType.ROLE, "role", "Роля", true),

                Commands.slash("clear", "Изтрива съобщения")
                        .addOption(OptionType.INTEGER, "amount", "Брой съобщения", true),

                Commands.slash("slowmode", "Задава slowmode на канал")
                        .addOption(OptionType.INTEGER, "seconds", "Секунди (0-21600)", true),

                Commands.slash("lock", "Заключва канала (забранява писане)"),
                Commands.slash("unlock", "Отключва канала (позволява писане)"),

                // 🟢 Nickname – финално правилно
                Commands.slash("nickname", "Променя прякор на потребител")
                        .addOption(OptionType.USER, "user", "Потребител", true)
                        .addOption(OptionType.STRING, "name", "Нов прякор", false)
                        .addOption(OptionType.BOOLEAN, "reset", "Ресетва прякора", false),

                // 💬 Социални
                Commands.slash("report", "Изпраща репорт към модераторите")
                        .addOption(OptionType.STRING, "message", "Съобщение", true),

                Commands.slash("suggest", "Изпраща предложение към сървъра")
                        .addOption(OptionType.STRING, "suggestion", "Предложение", true),

                Commands.slash("invite", "Създава покана за сървъра")
                        .addOption(OptionType.STRING, "expires", "Време (пример: 10s, 5m, 1h, 1d, 1w)", true)
                        .addOption(OptionType.INTEGER, "uses", "Брой използвания (0 = без лимит)", true)

        ).queue();
    }
}
