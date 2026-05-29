package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class GiveRoleCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member moderator = event.getMember();
        Member bot = event.getGuild().getSelfMember();

        Member target = event.getOption("user").getAsMember();
        Role role = event.getOption("role").getAsRole();

        // --- Забрана за @everyone ---
        if (role.isPublicRole()) {
            event.reply("❌ Не можеш да даваш ролята @everyone.").setEphemeral(true).queue();
            return;
        }

        // --- Проверки ---
        if (!moderator.hasPermission(Permission.MANAGE_ROLES)) {
            event.reply("❌ Нямаш право да управляваш роли.").setEphemeral(true).queue();
            return;
        }

        if (!moderator.canInteract(role)) {
            event.reply("❌ Не можеш да дадеш роля, която е по-висока от твоята.").setEphemeral(true).queue();
            return;
        }

        if (!bot.canInteract(role)) {
            event.reply("❌ Не мога да дам тази роля (по-висока е от моята).").setEphemeral(true).queue();
            return;
        }

        // --- Дава роля ---
        event.getGuild().addRoleToMember(target, role).queue();

        // --- Embed ---
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.GREEN);
        embed.setTitle("🎉 Роля добавена");
        embed.addField("Потребител", target.getAsMention(), false);
        embed.addField("Роля", role.getAsMention(), false);
        embed.addField("Модератор", moderator.getAsMention(), false);

        event.replyEmbeds(embed.build()).queue();
        LogUtil.log(event.getGuild(), embed);
    }
}
