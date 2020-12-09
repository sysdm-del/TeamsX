package me.sysdm.net.teams.players;

import lombok.Getter;
import me.sysdm.net.teams.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TeamPlayer {

    @Getter
    private final Player player;

    @Getter
    private final UUID UUID;

    public TeamPlayer(Player player) {
        this.player = player;
        this.UUID = player.getUniqueId();
    }
    public TeamPlayer(UUID playerUUID) {
        this.player = Bukkit.getPlayer(playerUUID);
        this.UUID = player.getUniqueId();
    }

    public Team getTeam() {
        return TeamManager.getTeam(player.getUniqueId());
    }

    public boolean isInTeam() {
        return getTeam() != null;
    }

    public TeamPosition getPosition() {
        if(getTeam().getOwner().getUUID() == getUUID()) return TeamPosition.OWNER;
        else if(getTeam().getCoOwner().getUUID() == getUUID()) return TeamPosition.CO_OWNER;
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
