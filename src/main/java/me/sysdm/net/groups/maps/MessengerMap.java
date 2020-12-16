package me.sysdm.net.groups.maps;

import dev.morphia.Datastore;
import lombok.SneakyThrows;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.groups.Messenger;
import me.sysdm.net.mongo.MongoManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MessengerMap<T extends Messenger> {

    private final Class<T> type;

    private final Map<GroupPlayer, T> map = new HashMap<>();

    private final Datastore datastore = MongoManager.getDatastore();

    public MessengerMap(Class<T> type) {
        this.type = type;
        for(T messenger : datastore.createQuery(type)) {
            map.put(messenger.getGroupPlayer(), messenger);
        }
    }


    public Map<GroupPlayer, T> getMap() {
        return map;
    }

    @SneakyThrows
    public T get(GroupPlayer groupPlayer) {
        T messenger = getMap().get(groupPlayer);
        if(messenger == null) {
            messenger = type.newInstance();
            messenger.setGroupPlayer(groupPlayer);
            map.put(groupPlayer, messenger);
            datastore.save(messenger);
        }
        return messenger;
    }

}
