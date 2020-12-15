package me.sysdm.net.groups.teams;

import dev.morphia.annotations.Entity;
import lombok.Getter;
import lombok.Setter;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.Messenger;
import me.sysdm.net.groups.maps.GroupAndPlayerMap;
import me.sysdm.net.groups.GroupPosition;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.groups.maps.MessengerMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Entity
public class TeamPlayer implements GroupPlayer {

    @Getter
    @Setter
    private Player player;

    @Getter
    @Setter
    private UUID UUID;

    public TeamPlayer(Player player) {
        this.player = player;
        this.UUID = player.getUniqueId();
        save(this);
    }
    public TeamPlayer(UUID playerUUID) {
        this.player = Bukkit.getPlayer(playerUUID);
        this.UUID = player.getUniqueId();
        save(this);
    }


    @Override
    public boolean isInGroup() {
        return getGroup() == null;
    }


    @Override
    public Group getGroup() {
        GroupAndPlayerMap<TeamPlayer, Team> map = new GroupAndPlayerMap<>(TeamPlayer.class, Team.class);
        return map.getMap().get(this);
    }
    @Override
    public GroupPosition getPosition() {
        if(getGroup() == null) return null;
        if(getGroup().getOwner().getUUID().equals(getUUID())) return GroupPosition.OWNER;
        else if(getGroup().getCoOwner().getUUID().equals(getUUID())) return GroupPosition.CO_OWNER;
        else return GroupPosition.MEMBER;
    }



    @Override
    public void setPosition(GroupPosition position) {
        switch (position) {
            case MEMBER:
                if(getPosition() == GroupPosition.OWNER) {
                    if (getGroup().getCoOwner() == null) {
                        getGroup().setOwner(getGroup().getMembers().stream().findFirst().get());
                        getGroup().getMembers().remove(getGroup().getMembers().stream().findFirst().get());
                    } else {
                        getGroup().setOwner(getGroup().getCoOwner());
                        getGroup().setCoOwner(null);
                    }
                }

                getGroup().getMembers().add(this);
            case CO_OWNER:
                if(getPosition() == GroupPosition.OWNER) {
                    if (getGroup().getCoOwner() == null) {
                        getGroup().setOwner(getGroup().getMembers().stream().findFirst().get());
                        getGroup().getMembers().remove(getGroup().getMembers().stream().findFirst().get());
                    } else {
                        getGroup().setOwner(getGroup().getCoOwner());
                    }
                }
                getGroup().setCoOwner(this);
            case OWNER:
                if(getPosition() == GroupPosition.CO_OWNER) {
                    getGroup().setCoOwner(null);
                }
                if(getPosition() == GroupPosition.MEMBER) {
                    getGroup().getMembers().remove(this);
                }
                getGroup().setOwner(this);
        }
    }

    @Override
    public Messenger getMessenger()  {
        MessengerMap<TeamMessenger> map = new MessengerMap<>(TeamMessenger.class);
        return map.getMap().get(this);
    }


}
