package me.sysdm.net.teams.commands;

import lombok.Getter;
import me.lucko.helper.Commands;
import me.lucko.helper.command.Command;
import me.lucko.helper.text3.Text;
import me.sysdm.net.lang.LangMessages;
import me.sysdm.net.mongo.MongoManager;
import me.sysdm.net.teams.Messenger;
import me.sysdm.net.teams.Team;
import me.sysdm.net.teams.TeamManager;
import me.sysdm.net.teams.TeamPosition;
import me.sysdm.net.teams.players.PlayerManager;
import me.sysdm.net.teams.players.TeamPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang3.ArrayUtils;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TeamCommand {

    @Getter
    private final String permission = "teams.command";

    @Getter
    private final String[] commandNames = new String[]{"teams", "team"};

    @Getter
    private final Command command;
    @Getter
    private final Command command2;

    public TeamCommand() {
        this.command = Commands.create()
                .assertPermission(permission, LangMessages.NO_PERMISSION.getMessage())
                .assertPlayer()
                .handler(c -> {
                    Player player = c.sender();
                    TeamPlayer teamPlayer = PlayerManager.getPlayer(player.getUniqueId());
                    int length = c.args().size();
                    if(length == 0) {
                        c.reply(help());
                    }
                    if(!teamPlayer.isInTeam()) {
                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                    }
                    if(length == 1) {
                        switch (c.rawArg(0)) {
                            case "sethome":
                                if(teamPlayer.getPosition() == TeamPosition.MEMBER) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.CO_OWNER.name()));
                                teamPlayer.getTeam().setHomeLocation(player.getLocation());
                                break;
                            case "home":
                                if(teamPlayer.getTeam().getHomeLocation() == null) c.reply("&c&l[!] Your CoOwner or leader hasn't set an home location!");
                                player.teleport(teamPlayer.getTeam().getHomeLocation());
                                break;
                            case "disband":
                                if(teamPlayer.getPosition() == TeamPosition.MEMBER || teamPlayer.getPosition() == TeamPosition.CO_OWNER) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.OWNER.name()));
                                TeamManager.removeTeam(teamPlayer.getTeam());
                                c.reply(LangMessages.SUCCESSFULLY_DISBANDED_TEAM.getMessage());
                                break;
                            case "leave":
                                if(teamPlayer.getPosition() == TeamPosition.MEMBER) teamPlayer.getTeam().getMembers().remove(teamPlayer);
                                if(teamPlayer.getPosition() == TeamPosition.CO_OWNER) teamPlayer.getTeam().setCoOwner(null);
                                if(teamPlayer.getPosition() == TeamPosition.OWNER) c.reply(LangMessages.OWNER_CANNOT_LEAVE_TEAM.getMessage());
                                c.reply(LangMessages.SUCCESSFULLY_LEFT_TEAM.getMessage());
                                break;
                            case "who":
                                if(!teamPlayer.isInTeam()) c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                String ownerName = teamPlayer.getTeam().getOwner().getPlayer().getName();
                                String coOwnerName = teamPlayer.getTeam().getCoOwner().getPlayer().getName();
                                String teamName = teamPlayer.getTeam().getTeamName();
                                StringBuilder members = new StringBuilder();
                                for (TeamPlayer tp : teamPlayer.getTeam().getMembers()) {
                                    members.append(tp.getPlayer().getName()).append("\n");
                                }
                                player.sendMessage(Text.colorize("&6&l----&d&lTeams&6&l----&r\n" +
                                        ChatColor.BOLD + ChatColor.AQUA + teamName + "\n&6Owner: " + ownerName + "\n&6CoOwner: " + coOwnerName + "\nMembers:\n" + members.toString()));
                            default: c.reply(help());
                        }
                    }
                    if(length == 2) {
                        switch (c.rawArg(0)) {
                            case "join":
                                if(TeamManager.getTeam(c.rawArg(1)) == null) c.reply(LangMessages.TEAM_DOESNT_EXIST.getMessage());
                                teamPlayer.getMessenger().sendRequest(TeamManager.getTeam(c.rawArg(1)).getOwner().getMessenger());
                                teamPlayer.getMessenger().sendRequest(TeamManager.getTeam(c.rawArg(1)).getCoOwner().getMessenger());
                                c.reply(LangMessages.SUCCESSFULLY_SENT_REQUEST.getMessage().replace("{team}", teamPlayer.getTeam().getTeamName()));
                                break;
                            case "invite":
                                if(!teamPlayer.isInTeam()) c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                if(Bukkit.getPlayerExact(c.rawArg(2)) == null) c.reply(LangMessages.PLAYER_DOES_NOT_EXIST.getMessage());
                                teamPlayer.getMessenger().sendInvite(PlayerManager.getPlayer(Bukkit.getPlayerExact(c.rawArg(2)).getUniqueId()).getMessenger());
                                c.reply(LangMessages.SUCCESSFULLY_SENT_INVITE.getMessage().replace("{team}", teamPlayer.getTeam().getTeamName()));
                            case "create":
                                if(teamPlayer.isInTeam()) {
                                    if(teamPlayer.getPosition() == TeamPosition.OWNER) {
                                        c.reply(LangMessages.OWNER_CANNOT_LEAVE_TEAM.getMessage());
                                    }
                                    if(teamPlayer.getPosition() == TeamPosition.CO_OWNER) {
                                        teamPlayer.getTeam().setCoOwner(null);
                                    }
                                    teamPlayer.getTeam().getMembers().remove(teamPlayer);
                                }
                                new Team(teamPlayer, c.rawArg(1), ObjectId.get());
                                c.reply(LangMessages.SUCCESSFULLY_CREATED_TEAM.getMessage().replace("{team}", teamPlayer.getTeam().getTeamName()));
                                break;
                            case "kick":
                                if(teamPlayer.getPosition() == TeamPosition.MEMBER) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.CO_OWNER.name()));
                                if(Bukkit.getPlayerExact(c.rawArg(2)) == null) c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                                if(!teamPlayer.getTeam().getMembers().contains(PlayerManager.getPlayer(Bukkit.getPlayerExact(c.rawArg(2)).getUniqueId()))) c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                                teamPlayer.getTeam().getMembers().remove(PlayerManager.getPlayer(Bukkit.getPlayerExact(c.rawArg(2)).getUniqueId()));
                                break;
                            case "who":
                                Team team = TeamManager.getTeam(c.rawArg(1));
                                if(team == null) c.reply(LangMessages.TEAM_DOESNT_EXIST.getMessage());
                                String ownerName = team.getOwner().getPlayer().getName();
                                String coOwnerName = team.getCoOwner().getPlayer().getName();
                                String teamName = team.getTeamName();
                                StringBuilder members = new StringBuilder();
                                for (TeamPlayer tp : team.getMembers()) {
                                    members.append(tp.getPlayer().getName()).append("\n");
                                }
                                player.sendMessage(Text.colorize("&6&l----&d&lTeams&6&l----&r\n" +
                                        ChatColor.BOLD + ChatColor.AQUA + teamName + "\n&6Owner: " + ownerName + "\n&6CoOwner: " + coOwnerName + "\nMembers:\n" + members.toString()));
                            case "rename":
                                if(teamPlayer.getPosition() == TeamPosition.MEMBER) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.CO_OWNER.name()));
                                teamPlayer.getTeam().setTeamName(c.rawArg(1));
                                c.reply(LangMessages.SUCCESSFULLY_RENAMED_TEAM.getMessage().replace("{team}", teamPlayer.getTeam().getTeamName()));
                            default: c.reply(help());
                        }
                    }
                    if(length == 3) {
                        if ("set".equals(c.rawArg(0))) {
                            if(teamPlayer.getPosition() == TeamPosition.MEMBER) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.CO_OWNER.name()));
                            if(Bukkit.getPlayerExact(c.rawArg(2)) == null) c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                            if(!teamPlayer.getTeam().getMembers().contains(PlayerManager.getPlayer(Bukkit.getPlayerExact(c.rawArg(2)).getUniqueId()))) c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                            Player p = Bukkit.getPlayerExact(c.rawArg(2));
                            TeamPlayer tp = PlayerManager.getPlayer(p.getUniqueId());
                            if(tp.getTeam().getOwner().getUUID() == tp.getUUID()) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.OWNER.name()));
                            switch (c.rawArg(2)) {
                                case "MEMBER":
                                    if(tp.getPosition() == TeamPosition.MEMBER) c.reply(LangMessages.PLAYER_ALREADY_IN_POSITION.getMessage().replace("{position}", tp.getPosition().name()));
                                    tp.setPosition(TeamPosition.MEMBER);
                                case "CO_OWNER":
                                    if(tp.getPosition() == TeamPosition.CO_OWNER) c.reply(LangMessages.PLAYER_ALREADY_IN_POSITION.getMessage().replace("{position}", tp.getPosition().name()));
                                    tp.setPosition(TeamPosition.CO_OWNER);
                                case "OWNER":
                                    if(teamPlayer.getPosition() == TeamPosition.CO_OWNER) c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", TeamPosition.OWNER.name()));
                                    if(tp.getPosition() == TeamPosition.OWNER) c.reply(LangMessages.PLAYER_ALREADY_IN_POSITION.getMessage().replace("{position}", tp.getPosition().name()));
                                    tp.setPosition(TeamPosition.OWNER);
                            }
                        }
                        c.reply(help());
                    }
                });
        this.command2 = Commands.create()
                .assertConsole()
                .handler(c -> {
                    if(c.args().size() > 1) {
                        if(TeamManager.getTeam(c.rawArg(1)) == null) c.reply("Team is null");
                        if(Bukkit.getPlayerExact(c.rawArg(2)) == null) c.reply("Player is null");
                        TeamManager.getTeam(c.rawArg(1)).getMembers().add(PlayerManager.getPlayer(Bukkit.getPlayerExact(c.rawArg(2)).getUniqueId()));
                    }
                });
    }

    private String help() {
        return "&6&l----&d&lTeams&6&l----&r\n&6&l/teams create <name> - Creates a team (Chat colors allowed)\n" +
                "&6&l/teams disband - Removes your current team if you're in one\n/teams join <name> - Requests to join a team\n" +
                "&6&l/teams sethome - Sets your teams home \n/teams home - Teleports you to the team home\n" +
                "&6&l/teams kick <member> - Kicks a member from the team\n/teams set <player> <MEMBER|CO_OWNER|OWNER>\n/teams invite <player> - Invite a player to your team\n/teams leave - Leave your current team\n" +
                "&6&l/teams who [team] - Shows the info of your team or another team\n /teams rename <newName> - Renames your team";
    }




}
