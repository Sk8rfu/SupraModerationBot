package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class HelpCommand {

    public static void run(SlashCommandInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("📘 Помощно меню");
        embed.setColor(Color.BLUE);

        embed.setDescription("Всички команди са подредени по категории за по‑лесно използване.");

        // 🟦 Информационни команди
        embed.addField("📘 Информация",
                """
                **/ping** – Показва latency
                **/help** – Показва това меню
                **/userinfo** – Информация за потребител
                **/serverinfo** – Информация за сървъра
                **/avatar** – Показва аватар
                **/about** – Информация за бота
                """,
                false);

        // 🟥 Модерация
        embed.addField("🛡 Модерация",
                """
                **/ban** – Банва потребител
                **/tempban** – Временен бан (1s, 1m, 1h, 1d, 1w)
                **/unban** – Премахва бан
                **/banid** – Банва потребител по ID
                **/banlist** – Показва списък с баннати потребители
                **/kick** – Киква потребител
                **/mute** – Мютва (timeout)
                **/unmute** – Размютва (timeout)
                **/muterole** – Мютва чрез роля
                **/unmuterole** – Размютва чрез роля
                **/warn** – Предупреждава потребител
                **/unwarn** – Премахва всички предупреждения
                **/warnings** – Показва предупрежденията на потребител
                """,
                false);

        // 🟩 Управление на сървъра
        embed.addField("⚙ Управление",
                """
                **/giverole** – Дава роля
                **/removerole** – Маха роля
                **/createrole** – Създава роля
                **/editrole** – Редактира роля
                **/deleterole** – Изтрива роля
                **/clear** – Изтрива съобщения
                **/slowmode** – Задава slowmode
                **/lock** – Заключва канал
                **/unlock** – Отключва канал
                **/nickname** – Променя прякор
                **/invite** – Създава покана (1s, 1m, 1h, 1d, 1w)
                """,
                false);

        // 🟨 Социални / комуникация
        embed.addField("💬 Социални",
                """
                **/report** – Изпраща репорт към модераторите
                **/suggest** – Изпраща предложение
                """,
                false);

        embed.setFooter("SupraModerationBot • Помощно меню", event.getJDA().getSelfUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
    }
}
