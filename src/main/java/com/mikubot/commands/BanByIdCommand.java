package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class BanByIdCommand {

    public static void run(SlashCommandInteractionEvent event) {

        String userId = event.getOption("userid").getAsString();
        String reason = event.getOption("reason") != null
                ? event.getOption("reason").getAsString()
                : "Няма посочена причина";

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("❌ Нямаш право да банваш.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("❌ Нямам право да банвам потребители.").setEphemeral(true).queue();
            return;
        }

        if (!userId.matches("\\d{17,20}")) {
            event.reply("❌ Невалидно ID.").setEphemeral(true).queue();
            return;
        }

        if (userId.equals(event.getGuild().getOwnerId())) {
            event.reply("❌ Не можеш да баннеш собственика на сървъра.").setEphemeral(true).queue();
            return;
        }

        if (userId.equals(moderator.getId())) {
            event.reply("❌ Не можеш да баннеш себе си.").setEphemeral(true).queue();
            return;
        }

        if (userId.equals(bot.getId())) {
            event.reply("❌ Не мога да банна себе си.").setEphemeral(true).queue();
            return;
        }

        // Проверка за роли ако user е в сървъра
        Member targetMember = event.getGuild().getMemberById(userId);
        if (targetMember != null) {

            if (targetMember.hasPermission(Permission.ADMINISTRATOR)) {
                event.reply("❌ Не мога да банна администратор.").setEphemeral(true).queue();
                return;
            }

            if (!moderator.canInteract(targetMember)) {
                event.reply("❌ Не можеш да баннеш потребител с по-висока роля.").setEphemeral(true).queue();
                return;
            }

            if (!bot.canInteract(targetMember)) {
                event.reply("❌ Не мога да банна потребител с по-висока роля от моята.").setEphemeral(true).queue();
                return;
            }
        }

        // --- Банване (без DM) ---
        event.getGuild()
                .ban(UserSnowflake.fromId(userId), 0, TimeUnit.SECONDS)
                .reason(reason)
                .queue();

        // --- Public Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("🔨 Бан по ID");

        embed.addField("🆔 Потребител ID", userId, false);
        embed.addField("👑 Модератор", moderator.getAsMention(), false);
        embed.addField("📝 Причина", reason, false);

        embed.setTimestamp(Instant.now());
        embed.setFooter("Извършено от " + moderator.getUser().getAsTag(),
                moderator.getUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
