package me.sysdm.net.groups;

import dev.morphia.annotations.Entity;
import me.sysdm.net.groups.*;
import me.sysdm.net.groups.interfaces.Storeable;
import org.bukkit.entity.Player;

import java.util.UUID;

@Entity
public interface GroupPlayer extends Storeable {


    UUID getUUID();

    Group getGroup();

    boolean isInGroup();

    GroupPosition getPosition();

    Player getPlayer();

    void setPosition(GroupPosition groupPosition);


    void setPlayer(Player player);

    Messenger getMessenger();
}
