package me.sysdm.net.teams;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import me.sysdm.net.teams.interfaces.Storeable;
import me.sysdm.net.teams.players.TeamPlayer;
import org.bson.types.ObjectId;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Team implements Storeable {

    @Getter
    @Setter
    private TeamPlayer owner;
    @Getter
    private final Set<TeamPlayer> members = new HashSet<>();
    @Getter
    @Setter
    private Location homeLocation;
    @Getter
    @Setter
    private String teamName;
    @Getter
    @Setter
    private TeamPlayer coOwner;
    @Getter
    @Id
    private final ObjectId objectId;

    public Team(TeamPlayer owner, String teamName, ObjectId objectId) {
        this.objectId = objectId;
        this.owner = owner;
        this.teamName = teamName;
        save(this);
    }

    public boolean isInTeam(TeamPlayer teamPlayer) {
        return getMembers().contains(teamPlayer) || getOwner().getUuid() == teamPlayer.getUuid() || getCoOwner().getUuid() == teamPlayer.getUuid();
    }

}
