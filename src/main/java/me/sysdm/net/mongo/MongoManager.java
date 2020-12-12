package me.sysdm.net.mongo;


import com.mongodb.MongoClientURI;
import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import me.sysdm.net.teams.Messenger;
import me.sysdm.net.teams.Team;
import me.sysdm.net.teams.players.TeamPlayer;
import me.sysdm.net.utils.ConfigUtils;

public class MongoManager {

    private final static MongoClient client;
    final static Morphia morphia = new Morphia().map(Team.class, TeamPlayer.class, Messenger.class);
    private static final Datastore datastore;

    static {
        MongoClientURI uri = new MongoClientURI(ConfigUtils.getConfigFile("mongodb.yml").getString("mongodb-uri"));

        client = new MongoClient(uri);
        datastore = morphia.createDatastore(client, "teamsx");
        MapperOptions o = morphia.getMapper().getOptions();
        o.setStoreNulls(true);
        o.setStoreEmpties(true);
    }

    public static Datastore getDatastore() {
        datastore.ensureIndexes();
        return datastore;
    }
}
