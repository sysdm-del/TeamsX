package me.sysdm.net.groups.maps;

import dev.morphia.Datastore;
import dev.morphia.annotations.Converters;
import dev.morphia.converters.UUIDConverter;
import dev.morphia.query.Query;
import dev.morphia.query.UpdateOperations;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.Messenger;
import me.sysdm.net.mongo.MongoManager;
import me.sysdm.net.groups.GroupPlayer;

import java.util.HashMap;
import java.util.Map;

@Converters(UUIDConverter.class)
public class GroupMap<T extends Group> {

    private static final Datastore datastore = MongoManager.getDatastore();

    private final Map<String, T> groupMap = new HashMap<>();


    public Map<String, T> getMap() {
        return groupMap;
    }

    public GroupMap(Class<T> type) {
        for(T group : datastore.createQuery(type)) {
            groupMap.put(group.getGroupName(), group);
        }
    }

    public T get(String name) {
        return groupMap.get(name);
    }


}
