package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class AboutCommand {

    public static void run(SlashCommandInteractionEvent event) {

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.MAGENTA);
        embed.setTitle("ℹ️ За SupraModeration");

        embed.setDescription("""
                🤖 **SupraModeration — Модерация и управление**
                Лек, бърз и надежден Discord бот, създаден за модерни сървъри.

                🔧 **Функции**
                • Модерация (бан, мют, предупреждения)
                • Управление на канали (lock, unlock, slowmode)
                • Информация (userinfo, serverinfo, ping)
                • Роли и прякори
                • Лог система за всички действия

                👑 **Създател:** Sk8rfu
                🌐 **Версия:** 1.0.0
                """);

        embed.setFooter("SupraModerationBot • Създаден с ❤️ от Sk8rfu",
                event.getJDA().getSelfUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
    }
}
