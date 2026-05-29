package com.mikubot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class AvatarCommand {

    public static void run(SlashCommandInteractionEvent event) {

        var user = event.getOption("user") != null ?
                event.getOption("user").getAsUser() :
                event.getUser();

        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("🖼 Аватар на " + user.getName());
        embed.setImage(user.getEffectiveAvatarUrl());

        event.replyEmbeds(embed.build()).queue();
    }
}
