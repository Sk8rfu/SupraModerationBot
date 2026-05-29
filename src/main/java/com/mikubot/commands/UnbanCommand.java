package com.mikubot.commands;

import com.mikubot.util.LogUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.time.Instant;

public class UnbanCommand {

    public static void run(SlashCommandInteractionEvent event) {

        String rawInput = event.getOption("userid").getAsString();

        if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
            event.reply("❌ Нямаш право да премахваш банове.").setEphemeral(true).queue();
            return;
        }

        // 1) Ако е mention → взимаме ID
        String cleanedInput = rawInput;
        if (cleanedInput.startsWith("<@") && cleanedInput.endsWith(">")) {
            cleanedInput = cleanedInput.replaceAll("[^0-9]", "");
        }

        // 2) Ако е чисто ID → директно опитваме
        if (cleanedInput.matches("\\d{17,20}")) {
            final String finalId = cleanedInput;
            unban(event, finalId);
            return;
        }

        // 3) Ако е име → търсим в списъка с баннати
        final String finalInput = cleanedInput;

        event.getGuild().retrieveBanList().queue(bans -> {

            User bannedUser = bans.stream()
                    .map(ban -> ban.getUser())
                    .filter(user ->
                            user.getName().equalsIgnoreCase(finalInput) ||
                            user.getAsTag().equalsIgnoreCase(finalInput)
                    )
                    .findFirst()
                    .orElse(null);

            if (bannedUser == null) {
                event.reply("❌ Не намерих баннат потребител с име: **" + finalInput + "**")
                        .setEphemeral(true)
                        .queue();
                return;
            }

            final String finalId = bannedUser.getId();
            unban(event, finalId);
        });
    }

    private static void unban(SlashCommandInteractionEvent event, String userId) {

        String moderatorTag = event.getUser().getAsTag();
        String moderatorAvatar = event.getUser().getAvatarUrl();

        event.getGuild().unban(UserSnowflake.fromId(userId)).queue(
                success -> {
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(Color.GREEN);
                    embed.setTitle("♻ Бан премахнат");

                    embed.addField("👤 Потребител ID", userId, false);
                    embed.addField("👑 Модератор", moderatorTag, false);
                    embed.setTimestamp(Instant.now());
                    embed.setFooter("Извършено от " + moderatorTag, moderatorAvatar);

                    event.replyEmbeds(embed.build()).queue();
                    LogUtil.log(event.getGuild(), embed);
                },
                error -> event.reply("❌ Не намерих бан за този потребител.")
                        .setEphemeral(true)
                        .queue()
        );
    }
}
