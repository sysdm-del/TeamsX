package me.sysdm.net.groups.interfaces;

import dev.morphia.Datastore;
import me.sysdm.net.groups.Messenger;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.mongo.MongoManager;

public interface Storeable {

    Datastore datastore = MongoManager.getDatastore();

    default void save(Group group) {
        datastore.save(group);
    }

    default void save(GroupPlayer groupPlayer) {
       datastore.save(groupPlayer);
    }

    default void save(Messenger messenger) {
        datastore.save(messenger);
    }

}
