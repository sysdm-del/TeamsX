package me.sysdm.net.teams.chat;


import com.acrylic.version_latest.Messages.ChatUtils;
import me.sysdm.net.eventapi.events.Events;
import me.sysdm.net.teams.Team;
import me.sysdm.net.teams.TeamMessage;
import me.sysdm.net.teams.players.PlayerManager;
import me.sysdm.net.teams.players.TeamPlayer;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents {

    public static void onChat() {
        Events.listen(AsyncPlayerChatEvent.class)
                .handler(e -> {
                    TeamPlayer teamPlayer = PlayerManager.getPlayer(e.getPlayer().getUniqueId());
                    if(teamPlayer.isInTeam()) {
                        Team team = teamPlayer.getTeam();
                        StringBuilder members = new StringBuilder();
                        for (TeamPlayer tp: team.getMembers()) {
                            members.append(tp.getPlayer().getName()).append("\n");
                        }
                        String format = new TeamMessage("&b[" + team.getTeamName().toUpperCase() + "&b]").tooltip("&bTeam: " + team.getTeamName() + "\nOwner: " + team.getOwner().getPlayer().getName() +
                                "\nCoOwner: " + team.getCoOwner().getPlayer().getName() + "Members: \n" + members.toString()).then(e.getFormat()).getFormattedMessage();
                        e.setFormat(format);
                    }
                });
    }


}
