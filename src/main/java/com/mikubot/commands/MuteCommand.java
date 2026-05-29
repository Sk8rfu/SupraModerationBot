package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class MuteCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();
        String reason = event.getOption("reason") != null
                ? event.getOption("reason").getAsString()
                : "Няма посочена причина";

        int minutes = event.getOption("minutes") != null
                ? event.getOption("minutes").getAsInt()
                : 10;

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("❌ Нямаш право да мютваш потребители.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("❌ Нямам право да мютвам потребители.").setEphemeral(true).queue();
            return;
        }

        if (target == null) {
            event.reply("❌ Не мога да намеря този потребител.").setEphemeral(true).queue();
            return;
        }

        if (target.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("❌ Не мога да мютна администратор.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(target)) {
            event.reply("❌ Не можеш да мютнеш потребител с по-висока роля.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(target)) {
            event.reply("❌ Не мога да мютна потребител с по-висока роля от моята.").setEphemeral(true).queue();
            return;
        }

        // --- DM Embed ---
        EmbedBuilder dmEmbed = new EmbedBuilder();
        dmEmbed.setColor(Color.YELLOW);
        dmEmbed.setTitle("🔇 Беше мютнат!");
        dmEmbed.setDescription("Получаваш това съобщение, защото беше временно мютнат.");
        dmEmbed.addField("🏰 Сървър", event.getGuild().getName(), false);
        dmEmbed.addField("⏳ Продължителност", minutes + " минути", false);
        dmEmbed.addField("👑 Модератор", moderator.getUser().getAsTag(), false);
        dmEmbed.addField("📝 Причина", reason, false);
        dmEmbed.setThumbnail(moderator.getUser().getAvatarUrl());
        dmEmbed.setTimestamp(Instant.now());

        // --- Изпращане на DM + fallback ---
        target.getUser().openPrivateChannel().queue(
                dm -> dm.sendMessageEmbeds(dmEmbed.build()).queue(
                        success -> {},
                        error -> event.reply("⚠️ Не успях да изпратя DM на потребителя — вероятно има изключени лични съобщения.")
                                .setEphemeral(true)
                                .queue()
                ),
                error -> event.reply("⚠️ Не успях да изпратя DM на потребителя — вероятно има изключени лични съобщения.")
                        .setEphemeral(true)
                        .queue()
        );

        // --- Timeout ---
        target.timeoutFor(Duration.ofMinutes(minutes)).reason(reason).queue();

        // --- Public Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.YELLOW);
        embed.setTitle("🔇 Потребител мютнат");

        embed.addField("👤 Потребител", target.getAsMention(), false);
        embed.addField("🆔 ID", target.getId(), false);
        embed.addField("⏳ Продължителност", minutes + " минути", false);
        embed.addField("📝 Причина", reason, false);
        embed.addField("👑 Модератор", moderator.getAsMention(), false);

        embed.setTimestamp(Instant.now());
        embed.setFooter("Извършено от " + moderator.getUser().getAsTag(),
                moderator.getUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
