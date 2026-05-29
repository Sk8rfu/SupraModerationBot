package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;

public class KickCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();
        String reason = event.getOption("reason") != null
                ? event.getOption("reason").getAsString()
                : "Няма посочена причина";

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("❌ Нямаш право да кикваш потребители.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.KICK_MEMBERS)) {
            event.reply("❌ Нямам право да киквам потребители.").setEphemeral(true).queue();
            return;
        }

        if (target == null) {
            event.reply("❌ Не мога да намеря този потребител.").setEphemeral(true).queue();
            return;
        }

        if (target.hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("❌ Не мога да кикна администратор.").setEphemeral(true).queue();
            return;
        }

        if (target.getId().equals(event.getGuild().getOwnerId())) {
            event.reply("❌ Не можеш да кикнеш собственика на сървъра.").setEphemeral(true).queue();
            return;
        }

        if (target.getId().equals(moderator.getId())) {
            event.reply("❌ Не можеш да кикнеш себе си.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(target)) {
            event.reply("❌ Не можеш да кикнеш потребител с по-висока роля.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(target)) {
            event.reply("❌ Не мога да кикна потребител с по-висока роля от моята.").setEphemeral(true).queue();
            return;
        }

        // --- DM Embed към потребителя ---
        EmbedBuilder dmEmbed = new EmbedBuilder();
        dmEmbed.setColor(Color.ORANGE);
        dmEmbed.setTitle("👢 Беше кикнат!");
        dmEmbed.setDescription("Получаваш това съобщение, защото беше кикнат от сървър.");
        dmEmbed.addField("🏰 Сървър", event.getGuild().getName(), false);
        dmEmbed.addField("👑 Модератор", moderator.getUser().getAsTag(), false);
        dmEmbed.addField("📝 Причина", reason, false);
        dmEmbed.setThumbnail(moderator.getUser().getAvatarUrl());
        dmEmbed.setTimestamp(Instant.now());

        // --- Изпращане на DM + тих fallback ---
        target.getUser().openPrivateChannel().queue(
                dm -> dm.sendMessageEmbeds(dmEmbed.build()).queue(
                        success -> {},
                        failure -> {} // игнорира DM грешката
                ),
                error -> {} // игнорира грешката при отваряне на DM
        );

        // --- Kick ---
        target.kick().reason(reason).queue();

        // --- Публичен Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.setTitle("👢 Потребител кикнат");

        embed.addField("👤 Потребител", target.getAsMention(), false);
        embed.addField("🆔 ID", target.getId(), false);
        embed.addField("👑 Модератор", moderator.getAsMention(), false);
        embed.addField("📝 Причина", reason, false);

        embed.setTimestamp(Instant.now());
        embed.setFooter("Извършено от " + moderator.getUser().getAsTag(),
                moderator.getUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
