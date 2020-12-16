package me.sysdm.net.events;

import dev.morphia.annotations.Converters;
import dev.morphia.converters.UUIDConverter;
import me.sysdm.net.eventapi.events.Events;
import me.sysdm.net.groups.Messenger;
import me.sysdm.net.groups.maps.GroupMap;
import me.sysdm.net.groups.maps.GroupPlayerMap;
import me.sysdm.net.groups.teams.Team;
import me.sysdm.net.groups.teams.TeamPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
@Converters(UUIDConverter.class)
public class TeamEvents implements TabCompleter {



    public static void onJoin() {
        Events.listen(PlayerJoinEvent.class)
                .handler(e -> {
                    GroupPlayerMap<TeamPlayer> groupPlayerMap = new GroupPlayerMap<>(TeamPlayer.class);
                    Messenger messenger = groupPlayerMap.get(e.getPlayer()).getMessenger();
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if(command.getName().equalsIgnoreCase("teams") || command.getName().equalsIgnoreCase("team")) {
            if(args.length == 1) {
                completions.add("sethome");
                completions.add("home");
                completions.add("disband");
                completions.add("leave");
                completions.add("who");
            }else if(args.length == 2) {
                completions.add("join");
                completions.add("invite");
                completions.add("create");
                completions.add("kick");
                completions.add("who");
            }else if(args.length == 3) {
                completions.add("set");
            }
        }

        return completions;
    }
}
