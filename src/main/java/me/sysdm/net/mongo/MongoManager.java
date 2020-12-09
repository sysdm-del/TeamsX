package me.sysdm.net.mongo;

import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import me.sysdm.net.teams.Messenger;
import me.sysdm.net.teams.Team;
import me.sysdm.net.teams.players.TeamPlayer;

public class MongoManager {
    private final static MongoClient client;
    final static Morphia morphia = new Morphia().map(Team.class, TeamPlayer.class, Messenger.class);
    private static final Datastore datastore;

    static {

        MongoClientURI uri = new MongoClientURI("");
        client = new MongoClient(uri);
        //TODO - fix URI
        datastore = morphia.createDatastore(client, "sysdm");
        MapperOptions o = morphia.getMapper().getOptions();
        o.setStoreNulls(true);
        o.setStoreEmpties(true);
    }

    public static Datastore getDatastore() {
        datastore.ensureIndexes();
        return datastore;
    }
}
