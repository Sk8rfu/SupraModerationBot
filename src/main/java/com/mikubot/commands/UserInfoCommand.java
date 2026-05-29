package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class UserInfoCommand {

    public static void run(SlashCommandInteractionEvent event) {

        Member member = event.getOption("user") != null ?
                event.getOption("user").getAsMember() :
                event.getMember();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setTitle("👤 Информация за " + member.getUser().getName());

        embed.addField("ID", member.getId(), false);
        embed.addField("Създаден акаунт", member.getUser().getTimeCreated().toString(), false);
        embed.addField("Влязъл в сървъра", member.getTimeJoined().toString(), false);

        embed.setThumbnail(member.getEffectiveAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
    }
}
