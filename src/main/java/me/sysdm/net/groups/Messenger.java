package me.sysdm.net.groups;

import dev.morphia.annotations.Entity;
import me.sysdm.net.groups.interfaces.Storeable;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;
@Entity
public interface Messenger extends Storeable {


    Set<String> getInvites();

    Set<String> getRequests();

    GroupPlayer getGroupPlayer();

    UUID getUUID();

    boolean hasPendingInvite();

    boolean hasPendingRequest();

    void sendInvite(Messenger messenger);

    void sendRequest(Messenger
                             messenger);

    void setGroupPlayer(GroupPlayer player);


    String requestMessage(Group group, Player requester, Messenger messenger);

    String inviteMessage(Group group, Player inviter, Messenger messenger);

}
