package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class RemoveRoleCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member target = event.getOption("user").getAsMember();
        Role role = event.getOption("role").getAsRole();

        // --- Забрана за @everyone ---
        if (role.isPublicRole()) {
            event.reply("❌ Не можеш да махаш ролята @everyone.").setEphemeral(true).queue();
            return;
        }

        if (!event.getMember().hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямаш право да управляваш роли.").setEphemeral(true).queue();
            return;
        }

        if (!event.getGuild().getSelfMember().canInteract(role)) {
            event.reply("❌ Не мога да махна тази роля (по-висока е от моята).").setEphemeral(true).queue();
            return;
        }

        event.getGuild().removeRoleFromMember(target, role).queue();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setTitle("🗑 Роля премахната");
        embed.addField("Потребител", target.getAsMention(), false);
        embed.addField("Роля", role.getAsMention(), false);
        embed.addField("Модератор", event.getUser().getAsMention(), false);

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
