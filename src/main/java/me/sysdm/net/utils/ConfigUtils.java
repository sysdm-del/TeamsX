package me.sysdm.net.utils;

import me.sysdm.net.TeamsX;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigUtils {

    public static FileConfiguration getConfigFile(String name) {
        TeamsX.getInstance().getDataFolder().mkdirs();
        File file = new File(TeamsX.getInstance().getDataFolder(), name);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            TeamsX.getInstance().saveResource(name, false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }


}
