package com.mikubot.commands;

import com.mikubot.util.WarnUtil;
import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class WarnCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();
        String reason = event.getOption("reason") != null
                ? event.getOption("reason").getAsString()
                : "Няма посочена причина";

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("❌ Нямаш право да предупреждаваш.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("❌ Нямам право да предупреждавам потребители.").setEphemeral(true).queue();
            return;
        }

        if (target == null) {
            event.reply("❌ Не мога да намеря този потребител.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(target)) {
            event.reply("❌ Не можеш да предупредиш потребител с по-висока роля.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(target)) {
            event.reply("❌ Не мога да предупредя потребител с по-висока роля от моята.").setEphemeral(true).queue();
            return;
        }

        // --- Запис в JSON ---
        WarnUtil.addWarn(target.getId(), reason);

        // --- Проверка за автоматични наказания ---
        int warnCount = WarnUtil.getWarns(target.getId()).size();
        String autoPunishment = "Няма автоматично наказание.";

        if (warnCount == 3) {
            target.timeoutFor(Duration.ofMinutes(10))
                    .reason("Автоматичен mute при 3 предупреждения")
                    .queue();
            autoPunishment = "🔇 Автоматичен **mute** (3 предупреждения)";
        }

        if (warnCount == 5) {
            target.kick().reason("Автоматичен kick при 5 предупреждения").queue();
            autoPunishment = "👢 Автоматичен **kick** (5 предупреждения)";
        }


        if (warnCount == 7) {
            event.getGuild().ban(target, 0, TimeUnit.SECONDS)
                    .reason("Автоматичен бан при 7 предупреждения")
                    .queue();
            autoPunishment = "🔨 Автоматичен **бан** (7 предупреждения)";
        }

        // --- DM към потребителя ---
        target.getUser().openPrivateChannel().queue(
        dm -> dm.sendMessage("⚠ Получи предупреждение в **" + event.getGuild().getName() + "**.\nПричина: " + reason).queue(
                success -> {},
                error -> {} // DM не може да се изпрати → игнорира
        ),
        error -> {} // DM каналът не може да се отвори → игнорира
);

        // --- Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("⚠ Предупреждение");

        embed.addField("👤 Потребител", target.getAsMention(), false);
        embed.addField("🆔 ID", target.getId(), false);
        embed.addField("📝 Причина", reason, false);
        embed.addField("📄 Общо предупреждения", String.valueOf(warnCount), false);
        embed.addField("⚙ Автоматично наказание", autoPunishment, false);
        embed.addField("👑 Модератор", moderator.getAsMention(), false);

        embed.setTimestamp(Instant.now());
        embed.setFooter("Извършено от " + moderator.getUser().getAsTag(),
                moderator.getUser().getAvatarUrl());

        // --- Изпращане към модератора ---
        event.replyEmbeds(embed.build()).queue();

        // --- Лог в mod-logs ---
        LogUtil.log(event.getGuild(), embed);
    }
}
