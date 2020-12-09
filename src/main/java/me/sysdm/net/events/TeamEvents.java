package me.sysdm.net.events;

import me.lucko.helper.Events;
import me.sysdm.net.teams.Messenger;
import me.sysdm.net.teams.players.PlayerManager;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeamEvents {

    public static void onJoin() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(e -> {
                    Messenger messenger = PlayerManager.getPlayer(e.getPlayer().getUniqueId()).getMessenger();
                    if(messenger.hasPendingInvite() || messenger.hasPendingRequest()) {
                        for(String invite : messenger.getInvites()) {
                            for(String request : messenger.getRequests()) {
                                e.getPlayer().sendMessage(invite);
                                e.getPlayer().sendMessage(request);
                            }
                        }
                    }
                });
    }

}
