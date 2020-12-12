package me.sysdm.net.teams.players;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import lombok.Getter;
import me.sysdm.net.teams.*;
import me.sysdm.net.teams.interfaces.Storeable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Entity
public class TeamPlayer implements Storeable {

    @Getter
    private final Player player;

    @Getter
    @Id
    private final UUID uuid;

    public TeamPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        save(this);
    }
    public TeamPlayer(UUID playerUUID) {
        this.player = Bukkit.getPlayer(playerUUID);
        this.uuid = player.getUniqueId();
        save(this);
    }

    public Team getTeam() {
        return TeamManager.getTeam(player.getUniqueId());
    }

    public boolean isInTeam() {
        return getTeam() != null;
    }

    public TeamPosition getPosition() {
        if(getTeam() == null) return null;
        if(getTeam().getOwner().getUuid().equals(getUuid())) return TeamPosition.OWNER;
        else if(getTeam().getCoOwner().getUuid().equals(getUuid())) return TeamPosition.CO_OWNER;
        else return TeamPosition.MEMBER;
    }

    public void setPosition(TeamPosition position) {
        switch (position) {
            case MEMBER:
                if(getPosition() == TeamPosition.OWNER) {
                    if (getTeam().getCoOwner() == null) {
                        getTeam().setOwner(getTeam().getMembers().stream().findFirst().get());
                        getTeam().getMembers().remove(getTeam().getMembers().stream().findFirst().get());
                    } else {
                        getTeam().setOwner(getTeam().getCoOwner());
                        getTeam().setCoOwner(null);
                    }
                }

                getTeam().getMembers().add(this);
            case CO_OWNER:
                if(getPosition() == TeamPosition.OWNER) {
                    if (getTeam().getCoOwner() == null) {
                        getTeam().setOwner(getTeam().getMembers().stream().findFirst().get());
                        getTeam().getMembers().remove(getTeam().getMembers().stream().findFirst().get());
                    } else {
                        getTeam().setOwner(getTeam().getCoOwner());
                    }
                }
                getTeam().setCoOwner(this);
            case OWNER:
                if(getPosition() == TeamPosition.CO_OWNER) {
                    getTeam().setCoOwner(null);
                }
                if(getPosition() == TeamPosition.MEMBER) {
                    getTeam().getMembers().remove(this);
                }
                getTeam().setOwner(this);
        }
    }


    public Messenger getMessenger()  {
        return TeamManager.getMessenger(this);
    }
}
