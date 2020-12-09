package me.sysdm.net.teams;

import dev.morphia.annotations.Id;
import lombok.Getter;
import lombok.Setter;
import me.sysdm.net.teams.players.TeamPlayer;
import org.bson.types.ObjectId;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Team {

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
    private final ObjectId objectId;

    public Team(TeamPlayer owner, String teamName, ObjectId objectId) {
        this.objectId = objectId;
        this.owner = owner;
        this.teamName = teamName;
    }



}
