package me.sysdm.net.groups;

import dev.morphia.annotations.Entity;
import me.sysdm.net.groups.interfaces.Storeable;
import org.bson.types.ObjectId;
import org.bukkit.Location;

import java.util.Set;

@Entity
public interface Group extends Storeable {

    GroupPlayer getOwner();

    Set<GroupPlayer> getMembers();

    Location getHomeLocation();

    String getGroupName();

    GroupPlayer getCoOwner();

    ObjectId getObjectId();

    void setOwner(GroupPlayer groupPlayer);

    void setCoOwner(GroupPlayer groupPlayer);

    void setHomeLocation(Location location);

    void setGroupName(String newName);

    boolean isInGroup(GroupPlayer groupPlayer);

}
