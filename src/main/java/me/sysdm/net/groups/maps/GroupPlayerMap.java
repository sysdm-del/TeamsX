package me.sysdm.net.groups.maps;

import dev.morphia.Datastore;
import lombok.SneakyThrows;
import me.sysdm.net.groups.Messenger;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.mongo.MongoManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GroupPlayerMap<T extends GroupPlayer> {

    private final Class<T> type;

    private final Map<Player, T> playerMap = new HashMap<>();

    private final Datastore datastore = MongoManager.getDatastore();


    public GroupPlayerMap(Class<T> type) {
        this.type = type;
        for(T player : datastore.createQuery(type)) {
            playerMap.put(player.getPlayer(), player);
        }
    }

    public Map<Player, T> getMap() {
        return playerMap;
    }

    @SneakyThrows
    public T get(Player player) {
       T gp = playerMap.get(player);
        if(gp == null) {
            gp = type.newInstance();
            gp.setPlayer(player);
            playerMap.put(player, gp);
            datastore.save(gp);
        }
        return gp;
    }



}
