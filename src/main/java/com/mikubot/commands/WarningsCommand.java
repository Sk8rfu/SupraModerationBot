package com.mikubot.commands;

import com.mikubot.util.WarnUtil;
import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class WarningsCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();

        if (target == null) {
            event.reply("❌ Не мога да намеря този потребител.").setEphemeral(true).queue();
            return;
        }

        List<String> warns = WarnUtil.getWarns(target.getId());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.ORANGE);
        embed.setTitle("📄 Предупреждения");

        embed.addField("👤 Потребител", target.getAsMention(), false);
        embed.addField("🆔 ID", target.getId(), false);

        if (warns.isEmpty()) {
            embed.addField("⚠ Няма предупреждения", "Този потребител няма записани предупреждения.", false);
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < warns.size(); i++) {
                sb.append("**").append(i + 1).append(".** ").append(warns.get(i)).append("\n");
            }
            embed.addField("⚠ Предупреждения", sb.toString(), false);
        }

        embed.setTimestamp(Instant.now());
        embed.setFooter("Справка за предупреждения");

        event.replyEmbeds(embed.build()).queue();

        // --- Лог в mod-logs ---
        LogUtil.log(event.getGuild(), embed);
    }
}
