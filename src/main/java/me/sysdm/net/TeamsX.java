package me.sysdm.net;

import me.sysdm.net.events.EventManager;
import me.sysdm.net.teams.commands.TeamCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamsX extends JavaPlugin {

    private static TeamsX instance;

    @Override
    public void onEnable() {
        instance = this;
        EventManager.setupEvents();
        new TeamCommand();
    }

    @Override
    public void onDisable() {

    }

    public static TeamsX getInstance() {
        return instance;
    }


}
