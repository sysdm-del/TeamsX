package me.sysdm.net.teams.players;

import dev.morphia.Datastore;
import dev.morphia.annotations.Converters;
import dev.morphia.converters.UUIDConverter;
import me.sysdm.net.mongo.MongoManager;
import me.sysdm.net.teams.Team;

import java.util.UUID;
@Converters(UUIDConverter.class)
public class PlayerManager {

    private static final Datastore datastore = MongoManager.getDatastore();

    public static TeamPlayer getPlayer(UUID uuid) {
        TeamPlayer player = datastore.createQuery(TeamPlayer.class).filter("UUID==", uuid).get();
        if(player == null) {
            return new TeamPlayer(uuid);
        }
        return player;
    }


}
