package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TempBanCommand {

    // Scheduler за delayed задачи
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();
        String timeRaw = event.getOption("time").getAsString();
        String reason = event.getOption("reason") != null
                ? event.getOption("reason").getAsString()
                : "Няма посочена причина";

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("❌ Нямаш право да банваш потребители.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("❌ Нямам право да банвам потребители.").setEphemeral(true).queue();
            return;
        }

        if (target == null) {
            event.reply("❌ Не мога да намеря този потребител.").setEphemeral(true).queue();
            return;
        }

        if (target.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("❌ Не мога да банна администратор.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(target)) {
            event.reply("❌ Не можеш да баннеш потребител с по-висока роля.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(target)) {
            event.reply("❌ Не мога да банна потребител с по-висока роля от моята.").setEphemeral(true).queue();
            return;
        }

        // --- Парсване на време ---
        long seconds = parseTime(timeRaw);
        if (seconds <= 0) {
            event.reply("❌ Невалиден формат! Използвай: `1s`, `1m`, `1h`, `1d`, `1w`").setEphemeral(true).queue();
            return;
        }

        // --- DM Embed ---
        EmbedBuilder dmEmbed = new EmbedBuilder();
        dmEmbed.setColor(Color.RED);
        dmEmbed.setTitle("⏳ Временен бан!");
        dmEmbed.setDescription("Получаваш това съобщение, защото беше временно баннат от сървър.");
        dmEmbed.addField("🏰 Сървър", event.getGuild().getName(), false);
        dmEmbed.addField("⏳ Продължителност", formatTime(timeRaw), false);
        dmEmbed.addField("👑 Модератор", moderator.getUser().getAsTag(), false);
        dmEmbed.addField("📝 Причина", reason, false);
        dmEmbed.setThumbnail(moderator.getUser().getAvatarUrl());
        dmEmbed.setTimestamp(Instant.now());

        // --- DM + тих fallback ---
        target.getUser().openPrivateChannel().queue(
                dm -> dm.sendMessageEmbeds(dmEmbed.build()).queue(
                        success -> {},
                        failure -> {} // игнорира DM грешката
                ),
                error -> {} // игнорира грешката при отваряне на DM
        );

        // --- Банване ---
        event.getGuild()
                .ban(UserSnowflake.fromId(target.getId()), 0, TimeUnit.SECONDS)
                .reason(reason)
                .queue();

        // --- Задача след изтичане ---
        scheduler.schedule(() -> {

            // 1) Автоматичен UNBAN
            event.getGuild().unban(UserSnowflake.fromId(target.getId()))
                    .reason("Tempban е изтекъл вече")
                    .queue();

            // 2) DM при изтичане
            event.getJDA().retrieveUserById(target.getId()).queue(user -> {

                EmbedBuilder unbanDM = new EmbedBuilder();
                unbanDM.setColor(Color.GREEN);
                unbanDM.setTitle("🔓 Tempban изтече");
                unbanDM.setDescription("Твоят временен бан в сървър **" + event.getGuild().getName() + "** приключи.");
                unbanDM.addField("⏳ Продължителност", formatTime(timeRaw), false);
                unbanDM.setTimestamp(Instant.now());

                user.openPrivateChannel().queue(
                        dm -> dm.sendMessageEmbeds(unbanDM.build()).queue(
                                success -> {},
                                failure -> {}
                        ),
                        error -> {}
                );
            });

            // 3) Лог при изтичане
            EmbedBuilder logEmbed = new EmbedBuilder();
            logEmbed.setColor(Color.GREEN);
            logEmbed.setTitle("🔓 Tempban изтече");
            logEmbed.setDescription("Потребителят беше автоматично unban-нат след изтичане на времето.");
            logEmbed.addField("👤 Потребител", target.getUser().getAsTag() + " (`" + target.getId() + "`)", false);
            logEmbed.addField("⏳ Продължителност", formatTime(timeRaw), false);
            logEmbed.addField("🏰 Сървър", event.getGuild().getName(), false);
            logEmbed.setTimestamp(Instant.now());

            LogUtil.log(event.getGuild(), logEmbed);

        }, seconds, TimeUnit.SECONDS);

        // --- Public Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("⏳ Временен бан");

        embed.addField("👤 Потребител", target.getAsMention(), false);
        embed.addField("🆔 ID", target.getId(), false);
        embed.addField("⏳ Продължителност", formatTime(timeRaw), false);
        embed.addField("📝 Причина", reason, false);
        embed.addField("👑 Модератор", moderator.getAsMention(), false);

        embed.setTimestamp(Instant.now());
        embed.setFooter("Извършено от " + moderator.getUser().getAsTag(),
                moderator.getUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }

    // --- Парсване на време ---
    private static long parseTime(String input) {
        input = input.toLowerCase();

        try {
            if (input.endsWith("s")) return Long.parseLong(input.replace("s", ""));
            if (input.endsWith("m")) return Long.parseLong(input.replace("m", "")) * 60;
            if (input.endsWith("h")) return Long.parseLong(input.replace("h", "")) * 3600;
            if (input.endsWith("d")) return Long.parseLong(input.replace("d", "")) * 86400;
            if (input.endsWith("w")) return Long.parseLong(input.replace("w", "")) * 604800;
        } catch (Exception ignored) {}

        return -1;
    }

    // --- Красив текст ---
    private static String formatTime(String input) {
        input = input.toLowerCase();

        if (input.endsWith("s")) return input.replace("s", "") + " секунди";
        if (input.endsWith("m")) return input.replace("m", "") + " минути";
        if (input.endsWith("h")) return input.replace("h", "") + " часа";
        if (input.endsWith("d")) return input.replace("d", "") + " дни";
        if (input.endsWith("w")) return input.replace("w", "") + " седмици";

        return "Неизвестно";
    }
}
