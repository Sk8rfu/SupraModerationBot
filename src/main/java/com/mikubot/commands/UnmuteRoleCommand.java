package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;

public class UnmuteRoleCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();
        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.MODERATE_MEMBERS)) {
            event.reply("❌ Нямаш право да размютваш.").setEphemeral(true).queue();
            return;
        }

        if (!bot.hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямам право да управлявам роли.").setEphemeral(true).queue();
            return;
        }

        if (target == null) {
            event.reply("❌ Не мога да намеря този потребител.").setEphemeral(true).queue();
            return;
        }

        Role muteRole = event.getGuild().getRolesByName("Muted", true)
                .stream().findFirst().orElse(null);

        if (muteRole == null) {
            event.reply("❌ Няма роля **Muted** в този сървър.").setEphemeral(true).queue();
            return;
        }

        if (!target.getRoles().contains(muteRole)) {
            event.reply("❌ Този потребител няма роля **Muted**.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(target)) {
            event.reply("❌ Не можеш да размютнеш потребител с по-висока роля.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(target)) {
            event.reply("❌ Не мога да размютна потребител с по-висока роля от моята.").setEphemeral(true).queue();
            return;
        }

        // --- DM Embed ---
        EmbedBuilder dmEmbed = new EmbedBuilder();
        dmEmbed.setColor(Color.GREEN);
        dmEmbed.setTitle("🔊 Беше размютнат!");
        dmEmbed.setDescription("Ролята **Muted** беше премахната и вече можеш да пишеш отново.");
        dmEmbed.addField("🏰 Сървър", event.getGuild().getName(), false);
        dmEmbed.addField("👑 Модератор", moderator.getUser().getAsTag(), false);
        dmEmbed.setThumbnail(moderator.getUser().getAvatarUrl());
        dmEmbed.setTimestamp(Instant.now());

        target.getUser().openPrivateChannel().queue(
                dm -> dm.sendMessageEmbeds(dmEmbed.build()).queue(
                        success -> {},
                        error -> {}
                ),
                error -> {}
        );

        // --- Премахване на ролята ---
        event.getGuild().removeRoleFromMember(target, muteRole).queue();

        // --- Public Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.GREEN);
        embed.setTitle("🔊 Потребител размютнат (роля)");

        embed.addField("👤 Потребител", target.getAsMention(), false);
        embed.addField("🆔 ID", target.getId(), false);
        embed.addField("👑 Модератор", moderator.getAsMention(), false);

        embed.setTimestamp(Instant.now());
        embed.setFooter("Извършено от " + moderator.getUser().getAsTag(),
                moderator.getUser().getAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
