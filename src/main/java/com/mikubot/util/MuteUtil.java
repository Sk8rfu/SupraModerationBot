package com.mikubot.util;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

public class MuteUtil {

    public static Role getOrCreateMuteRole(Guild guild) {

        Role muteRole = guild.getRolesByName("Muted", true)
                .stream().findFirst().orElse(null);

        if (muteRole != null)
            return muteRole;

        // Създаване на роля
        muteRole = guild.createRole()
                .setName("Muted")
                .setPermissions(Permission.EMPTY_PERMISSIONS)
                .complete();

        // Правим я final за lambda
        Role finalMuteRole = muteRole;

        // Забраняваме писане във всички канали
        guild.getTextChannels().forEach(channel -> {
            channel.upsertPermissionOverride(finalMuteRole)
                    .deny(Permission.MESSAGE_SEND)
                    .queue();
        });

        return muteRole;
    }
}
