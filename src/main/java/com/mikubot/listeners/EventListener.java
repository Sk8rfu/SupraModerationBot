package com.mikubot.listeners;

import com.mikubot.commands.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        switch (event.getName()) {

            case "ping" -> PingCommand.run(event);
            case "help" -> HelpCommand.run(event);
            case "userinfo" -> UserInfoCommand.run(event);
            case "serverinfo" -> ServerInfoCommand.run(event);
            case "avatar" -> AvatarCommand.run(event);

            case "ban" -> BanCommand.run(event);
            case "tempban" -> TempBanCommand.run(event);
            case "banid" -> BanByIdCommand.run(event);
            case "unban" -> UnbanCommand.run(event);
            case "banlist" -> BanListCommand.run(event);
            case "kick" -> KickCommand.run(event);

            case "mute" -> MuteCommand.run(event);
            case "unmute" -> UnmuteCommand.run(event);
            case "muterole" -> MuteRoleCommand.run(event);
            case "unmuterole" -> UnmuteRoleCommand.run(event);

            case "warn" -> WarnCommand.run(event);
            case "unwarn" -> UnwarnCommand.run(event);
            case "warnings" -> WarningsCommand.run(event);

            case "clear" -> ClearCommand.run(event);

            case "slowmode" -> SlowmodeCommand.run(event);
            case "lock" -> LockCommand.run(event);
            case "unlock" -> UnlockCommand.run(event);
            case "nickname" -> NicknameCommand.run(event);

            case "giverole" -> GiveRoleCommand.run(event);
            case "removerole" -> RemoveRoleCommand.run(event);

            // 🆕 Новите команди
            case "createrole" -> CreateRoleCommand.run(event);
            case "editrole" -> EditRoleCommand.run(event);
            case "deleterole" -> DeleteRoleCommand.run(event);

            case "invite" -> InviteCommand.run(event);
            case "report" -> ReportCommand.run(event);
            case "suggest" -> SuggestCommand.run(event);

            case "about" -> AboutCommand.run(event);

            default -> event.reply("❌ Непозната команда.").setEphemeral(true).queue();
        }
    }
}
