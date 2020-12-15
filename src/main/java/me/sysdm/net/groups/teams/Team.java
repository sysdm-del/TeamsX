package me.sysdm.net.groups.teams;

import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.GroupPlayer;
import org.bson.types.ObjectId;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class Team implements Group {

    @Getter
    @Setter
    private GroupPlayer owner;
    @Getter
    private final Set<GroupPlayer> members = new HashSet<>();
    @Getter
    @Setter
    private Location homeLocation;
    @Setter
    private String groupName;
    @Getter
    @Setter
    private GroupPlayer coOwner;
    @Getter
    @Id
    private final ObjectId objectId;

    public Team(TeamPlayer owner, String teamName, ObjectId objectId) {
        this.objectId = objectId;
        this.owner = owner;
        this.groupName = teamName;
        save(this);
    }

    @Override
    public String getGroupName() {
        return this.groupName;
    }

    @Override
    public boolean isInGroup(GroupPlayer groupPlayer) {
        return getMembers().contains(groupPlayer) || getOwner().getUUID() == groupPlayer.getUUID() || getCoOwner().getUUID() == groupPlayer.getUUID();
    }
}
