package me.sysdm.net.lang;

import com.google.common.base.CaseFormat;
import lombok.Getter;
import me.lucko.helper.text.Text;
import me.sysdm.net.utils.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;

public enum LangMessages {

    NO_PERMISSION(""), NOT_IN_TEAM("teams"), POSITION_TOO_LOW("teams"), SUCCESSFULLY_DISBANDED_TEAM("teams"), TEAM_DOESNT_EXIST("teams"),
    OWNER_CANNOT_LEAVE_TEAM("teams"), PLAYER_NOT_IN_TEAM("teams"), PLAYER_ALREADY_IN_POSITION("teams"), SUCCESSFULLY_LEFT_TEAM("teams"), SUCCESSFULLY_CREATED_TEAM("teams"),
    PLAYER_DOES_NOT_EXIST("teams"), SUCCESSFULLY_SENT_INVITE("teams"), SUCCESSFULLY_SENT_REQUEST("teams"), SUCCESSFULLY_RENAMED_TEAM("teams");

    @Getter
    private final String message;

    @Getter
    private final FileConfiguration config = ConfigUtils.getConfigFile("lang.yml");

    LangMessages(String path) {
        this.message = Text.colorize(config.getString(path.concat(path.equals("") ? "" : ".") + CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name())));
    }



}
