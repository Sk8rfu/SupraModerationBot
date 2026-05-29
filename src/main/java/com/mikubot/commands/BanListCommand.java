package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class BanListCommand {

    public static void run(SlashCommandInteractionEvent event) {

        // Проверка за права
        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("❌ Нямаш право да виждаш списъка с баннати.").setEphemeral(true).queue();
            return;
        }

        Guild guild = event.getGuild();

        guild.retrieveBanList().queue(bans -> {

            if (bans.isEmpty()) {
                event.reply("✅ Няма баннати потребители в този сървър.").setEphemeral(true).queue();
                return;
            }

            // Embed за списъка
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.RED);
            embed.setTitle("🔨 Списък с баннати потребители");
            embed.setTimestamp(Instant.now());
            embed.setFooter("Общо баннати: " + bans.size());

            StringBuilder description = new StringBuilder();

            for (Guild.Ban ban : bans) {
                User user = ban.getUser();
                String reason = ban.getReason() != null ? ban.getReason() : "❌ Няма посочена причина";

                description.append("**")
                        .append(user.getAsTag())
                        .append("** (`")
                        .append(user.getId())
                        .append("`)\n")
                        .append("📝 Причина: ")
                        .append(reason)
                        .append("\n")
                        .append("🔗 [Профил](https://discord.com/users/")
                        .append(user.getId())
                        .append(")\n\n");
            }

            embed.setDescription(description.toString());

            event.replyEmbeds(embed.build()).queue();
        });
    }
}
