package me.sysdm.net.groups.teams;

import com.sun.javafx.image.impl.ByteIndexed;
import dev.morphia.annotations.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lucko.helper.function.Predicates;
import me.lucko.helper.messaging.bungee.BungeeCordImpl;
import me.sysdm.net.eventapi.events.Events;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.GroupMessage;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.groups.Messenger;
import org.bukkit.entity.Player;

import java.sql.SQLData;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
public class TeamMessenger implements Messenger {

    @Getter
    private final Set<String> invites = new HashSet<>();

    @Getter
    private final Set<String> requests = new HashSet<>();

    @Setter
    @Getter
    private GroupPlayer groupPlayer;

    @Id
    @Getter
    @Setter
    private UUID UUID;


    public TeamMessenger(GroupPlayer player) {
        this.groupPlayer = player;
        this.UUID = player.getUUID();
        save(this);
    }



    @Override
    public boolean hasPendingInvite() {
        return invites.isEmpty();
    }

    @Override
    public boolean hasPendingRequest() {
        return requests.isEmpty();
    }

    @Override
    public void sendInvite(Messenger messenger) {
        messenger.getInvites().add(inviteMessage(groupPlayer.getGroup(), groupPlayer.getPlayer(), messenger));
    }

    @Override
    public void sendRequest(Messenger messenger) {
        messenger.getInvites().add(requestMessage(groupPlayer.getGroup(), groupPlayer.getPlayer(), messenger));
    }

    @Override
    public String requestMessage(Group group, Player requester, Messenger messenger) {
        return new GroupMessage("&a&l[!] " + requester.getName() + " wants to join you team!" + group.getGroupName() + " !" + " Click")
                .then("&6&l[here]").command("/teamadmin join " + group.getGroupName() + " " + messenger.getGroupPlayer().getPlayer().getName()).then(" &a&lto accept the invite.").getFormattedMessage();
    }

    @Override
    public String inviteMessage(Group group, Player inviter, Messenger messenger) {
        StringBuilder members = new StringBuilder();
        for (GroupPlayer groupPlayer : group.getMembers()) {
            members.append(groupPlayer.getPlayer().getName()).append("\n");
        }
        return new GroupMessage("&a&l[!] You have been invited by " + inviter.getName() + " to join their team " + group.getGroupName() + " !" + " Click")
                .then("&6&l[here]").tooltip("&bTeam: " + group.getGroupName() + "\nOwner: " + group.getOwner().getPlayer().getName() +
                        "\nCoOwner: " + group.getCoOwner().getPlayer().getName() + "Members: \n" + members.toString()).command("/teamadmin join " + group.getGroupName() + " " + messenger.getGroupPlayer().getPlayer().getName()).then(" &a&l to join").getFormattedMessage();
    }
}
