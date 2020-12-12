package me.sysdm.net.teams.interfaces;

import me.sysdm.net.teams.Messenger;
import me.sysdm.net.teams.Team;
import me.sysdm.net.teams.TeamManager;
import me.sysdm.net.teams.players.TeamPlayer;

public interface Storeable {

    default void save(Team team) {
        TeamManager.save(team);
    }

    default void save(TeamPlayer teamPlayer) {
        TeamManager.save(teamPlayer);
    }

    default void save(Messenger messenger) {
        TeamManager.save(messenger);
    }

}
