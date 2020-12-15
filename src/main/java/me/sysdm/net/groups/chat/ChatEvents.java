package me.sysdm.net.groups.chat;


import me.sysdm.net.eventapi.events.Events;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.GroupMessage;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.groups.maps.GroupPlayerMap;
import me.sysdm.net.groups.teams.TeamPlayer;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents {

    public static void onChat() {
        Events.listen(AsyncPlayerChatEvent.class)
                .handler(e -> {
                    GroupPlayerMap<TeamPlayer> map = new GroupPlayerMap<>(TeamPlayer.class);
                    TeamPlayer teamPlayer;
                    teamPlayer = map.get(e.getPlayer());
                    if(teamPlayer.isInGroup()) {
                        Group group = teamPlayer.getGroup();
                        StringBuilder members = new StringBuilder();
                        for (GroupPlayer tp: group.getMembers()) {
                            members.append(tp.getPlayer().getName()).append("\n");
                        }
                        String format = new GroupMessage("&b[" + group.getGroupName().toUpperCase() + "&b]").tooltip("&bTeam: " + group.getGroupName() + "\nOwner: " + group.getOwner().getPlayer().getName() +
                                "\nCoOwner: " + group.getCoOwner().getPlayer().getName() + "Members: \n" + members.toString()).then(e.getFormat()).getFormattedMessage();
                        e.setFormat(format);
                    }
                });
    }


}
