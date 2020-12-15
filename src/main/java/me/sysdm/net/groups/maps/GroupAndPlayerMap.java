package me.sysdm.net.groups.maps;

import dev.morphia.Datastore;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.mongo.MongoManager;

import java.util.HashMap;
import java.util.Map;

public class GroupAndPlayerMap<A extends GroupPlayer, B extends Group> {

    Class<A> a;

    Class<B> b;

    private final Map<A, B> map = new HashMap<>();

    private static final Datastore datastore = MongoManager.getDatastore();

    public GroupAndPlayerMap(Class<A> a, Class<B> b) {
        this.a = a;
        this.b = b;
    }


    public Map<A, B> getMap() {
        for(A player : datastore.createQuery(a)) {
            for(B group : datastore.createQuery(b)) {
                map.put(player, group);
            }
        }
        return map;
    }

    public B get(GroupPlayer groupPlayer) {
        return map.get(groupPlayer);
    }

}
