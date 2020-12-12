package me.sysdm.net.teams;

import dev.morphia.Datastore;
import dev.morphia.annotations.Converters;
import dev.morphia.converters.UUIDConverter;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import me.sysdm.net.mongo.MongoManager;
import me.sysdm.net.teams.players.PlayerManager;
import me.sysdm.net.teams.players.TeamPlayer;

import java.util.UUID;
@Converters(UUIDConverter.class)
public class TeamManager {


    private static final Datastore datastore = MongoManager.getDatastore();

    public static Team getTeam(UUID playerUUID) {
        TeamPlayer teamPlayer = PlayerManager.getPlayer(playerUUID);
        for(Team team : datastore.createQuery(Team.class)) {
            if(team.isInTeam(teamPlayer)) return team;
        }
        return null;
    }

    public static Messenger getMessenger(TeamPlayer teamPlayer) {
        if(datastore.createQuery(Messenger.class).filter("uuid ==", teamPlayer.getUuid()).get() == null) return new Messenger(teamPlayer);
        return datastore.createQuery(Messenger.class).filter("uuid ==", teamPlayer.getUuid()).get();
    }

    public static Team getTeam(String teamName) {
        for(Team team : datastore.createQuery(Team.class)) {
            if(teamName.equalsIgnoreCase(team.getTeamName())) return team;
        }
        return null;
    }

    public static void save(Team team) {
        datastore.save(team);
    }

    public static void save(TeamPlayer teamPlayer) {
        datastore.save(teamPlayer);
    }

    public static void save(Messenger messenger) {
        datastore.save(messenger);
    }

    public static void removeTeam(Team team) {
        UpdateOperations<Team> ops = datastore.createUpdateOperations(Team.class).disableValidation().removeAll("objectId==", team.getObjectId());
        Query<Team> query = datastore.createQuery(Team.class);
        datastore.update(query, ops);
    }



}
