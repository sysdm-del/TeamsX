package me.sysdm.net.groups.commands;

import lombok.Getter;
import me.lucko.helper.Commands;
import me.lucko.helper.command.Command;
import me.lucko.helper.text3.Text;
import me.sysdm.net.groups.Group;
import me.sysdm.net.groups.GroupPlayer;
import me.sysdm.net.groups.GroupPosition;
import me.sysdm.net.groups.maps.GroupMap;
import me.sysdm.net.groups.maps.GroupPlayerMap;
import me.sysdm.net.groups.teams.Team;
import me.sysdm.net.groups.teams.TeamPlayer;
import me.sysdm.net.lang.LangMessages;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
                        GroupPlayerMap<TeamPlayer> groupPlayerMap = new GroupPlayerMap<>(TeamPlayer.class);
                        GroupMap<Team> groupMap = new GroupMap<>(Team.class);
                        TeamPlayer teamPlayer = groupPlayerMap.getMap().get(player);
                        int length = c.args().size();
                        if (length == 0) {
                            c.reply(help());
                            return;
                        }
                        if (length == 1) {
                            switch (c.rawArg(0)) {
                                case "sethome":
                                    if (teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    if (teamPlayer.getPosition() == GroupPosition.MEMBER) {
                                        c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.CO_OWNER.name()));
                                        return;
                                    }
                                    teamPlayer.getGroup().setHomeLocation(player.getLocation());
                                    break;
                                case "home":
                                    if (teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    if (teamPlayer.getGroup().getHomeLocation() == null) {
                                        c.reply("&c&l[!] Your CoOwner or leader hasn't set an home location!");
                                        return;
                                    }
                                    player.teleport(teamPlayer.getGroup().getHomeLocation());
                                    break;
                                case "disband":
                                    if (teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                    }
                                    if (teamPlayer.getPosition() == GroupPosition.MEMBER || teamPlayer.getPosition() == GroupPosition.CO_OWNER) {
                                        c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.OWNER.name()));
                                        return;
                                    }
                                    groupMap.getMap().remove(teamPlayer.getGroup().getGroupName());
                                    c.reply(LangMessages.SUCCESSFULLY_DISBANDED_TEAM.getMessage());
                                    return;
                                case "leave":
                                    if (teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    if (teamPlayer.getPosition() == GroupPosition.MEMBER)
                                        teamPlayer.getGroup().getMembers().remove(teamPlayer);
                                    if (teamPlayer.getPosition() == GroupPosition.CO_OWNER)
                                        teamPlayer.getGroup().setCoOwner(null);
                                    if (teamPlayer.getPosition() == GroupPosition.OWNER) {
                                        c.reply(LangMessages.OWNER_CANNOT_LEAVE_TEAM.getMessage());
                                        return;
                                    }
                                    c.reply(LangMessages.SUCCESSFULLY_LEFT_TEAM.getMessage());
                                    return;
                                case "who":
                                    if (teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    String ownerName = teamPlayer.getGroup().getOwner().getPlayer().getName();
                                    String coOwnerName = teamPlayer.getGroup().getCoOwner().getPlayer().getName();
                                    String teamName = teamPlayer.getGroup().getGroupName();
                                    StringBuilder members = new StringBuilder();
                                    for (GroupPlayer tp : teamPlayer.getGroup().getMembers()) {
                                        members.append(tp.getPlayer().getName()).append("\n");
                                    }
                                    player.sendMessage(Text.colorize("&6&l----&d&lTeams&6&l----&r\n" +
                                            ChatColor.BOLD + ChatColor.AQUA + teamName + "\n&6Owner: " + ownerName + "\n&6CoOwner: " + coOwnerName + "\nMembers:\n" + members.toString()));
                                default:
                                    c.reply(help());
                            }
                        }
                        if (length == 2) {
                            switch (c.rawArg(0)) {
                                case "join":
                                    if (groupMap.getMap().get(c.rawArg(1)) == null) {
                                        c.reply(LangMessages.TEAM_DOESNT_EXIST.getMessage());
                                        return;
                                    }
                                    teamPlayer.getMessenger().sendRequest(groupMap.getMap().get(c.rawArg(1)).getOwner().getMessenger());
                                    teamPlayer.getMessenger().sendRequest(groupMap.getMap().get(c.rawArg(1)).getCoOwner().getMessenger());
                                    c.reply(LangMessages.SUCCESSFULLY_SENT_REQUEST.getMessage().replace("{team}", teamPlayer.getGroup().getGroupName()));
                                    return;
                                case "invite":
                                    if (teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    if (Bukkit.getPlayerExact(c.rawArg(2)) == null) {
                                        c.reply(LangMessages.PLAYER_DOES_NOT_EXIST.getMessage());
                                        return;
                                    }
                                    teamPlayer.getMessenger().sendInvite(groupPlayerMap.get(Bukkit.getPlayerExact(c.rawArg(2))).getMessenger()); c.reply(LangMessages.SUCCESSFULLY_SENT_INVITE.getMessage().replace("{team}", teamPlayer.getGroup().getGroupName()));
                                    return;
                                case "create":
                                    if (teamPlayer.isInGroup()) {
                                        if (teamPlayer.getPosition() == GroupPosition.OWNER) {
                                            c.reply(LangMessages.OWNER_CANNOT_LEAVE_TEAM.getMessage());
                                        }
                                        if (teamPlayer.getPosition() == GroupPosition.CO_OWNER) {
                                            teamPlayer.getGroup().setCoOwner(null);
                                        }
                                        teamPlayer.getGroup().getMembers().remove(teamPlayer);
                                    }
                                    Group playerGroup = new Team(teamPlayer, c.rawArg(1), ObjectId.get());
                                    c.reply(LangMessages.SUCCESSFULLY_CREATED_TEAM.getMessage().replace("{team}", playerGroup.getGroupName()));
                                    return;
                                case "kick":
                                    if (!teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                    }
                                    if (teamPlayer.getPosition() == GroupPosition.MEMBER) {
                                        c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.CO_OWNER.name()));
                                        return;
                                    }
                                    if (Bukkit.getPlayerExact(c.rawArg(2)) == null) {
                                        c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    if (!teamPlayer.getGroup().getMembers().contains(groupPlayerMap.get(Bukkit.getPlayerExact(c.rawArg(2))))) {
                                        c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    teamPlayer.getGroup().getMembers().remove(groupPlayerMap.get(Bukkit.getPlayerExact(c.rawArg(2))));
                                    return;
                                case "who":
                                    if (!teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    Group group = groupMap.get(c.rawArg(1));
                                    if (group == null) {
                                        c.reply(LangMessages.TEAM_DOESNT_EXIST.getMessage());
                                        return;
                                    }
                                    String ownerName = group.getOwner().getPlayer().getName();
                                    String coOwnerName = group.getCoOwner().getPlayer().getName();
                                    String teamName = group.getGroupName();
                                    StringBuilder members = new StringBuilder();
                                    for (GroupPlayer tp : group.getMembers()) {
                                        members.append(tp.getPlayer().getName()).append("\n");
                                    }
                                    player.sendMessage(Text.colorize("&6&l----&d&lTeams&6&l----&r\n" +
                                            ChatColor.BOLD + ChatColor.AQUA + teamName + "\n&6Owner: " + ownerName + "\n&6CoOwner: " + coOwnerName + "\nMembers:\n" + members.toString()));
                                    return;
                                case "rename":
                                    if (!teamPlayer.isInGroup()) {
                                        c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                        return;
                                    }
                                    if (teamPlayer.getPosition() == GroupPosition.MEMBER) {
                                        c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.CO_OWNER.name()));
                                        return;
                                    }
                                    teamPlayer.getGroup().setGroupName(c.rawArg(1));
                                    c.reply(LangMessages.SUCCESSFULLY_RENAMED_TEAM.getMessage().replace("{team}", teamPlayer.getGroup().getGroupName()));
                                    return;
                                default:
                                    c.reply(help());
                            }
                        }
                        if (length == 3) {
                            if ("set".equals(c.rawArg(0))) {
                                if (!teamPlayer.isInGroup()) {
                                    c.reply(LangMessages.NOT_IN_TEAM.getMessage());
                                }
                                if (teamPlayer.getPosition() == GroupPosition.MEMBER)
                                    c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.CO_OWNER.name()));
                                if (Bukkit.getPlayerExact(c.rawArg(2)) == null)
                                    c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                                if (!teamPlayer.getGroup().getMembers().contains(groupPlayerMap.get(Bukkit.getPlayerExact(c.rawArg(2)))))
                                    c.reply(LangMessages.PLAYER_NOT_IN_TEAM.getMessage());
                                Player p = Bukkit.getPlayerExact(c.rawArg(2));
                                GroupPlayer tp = groupPlayerMap.get(p);
                                if (tp.getPosition() == GroupPosition.CO_OWNER)
                                    c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.OWNER.name()));
                                switch (c.rawArg(2)) {
                                    case "MEMBER":
                                        if (tp.getPosition() == GroupPosition.MEMBER) {
                                            c.reply(LangMessages.PLAYER_ALREADY_IN_POSITION.getMessage().replace("{position}", tp.getPosition().name()));
                                            return;
                                        }
                                        tp.setPosition(GroupPosition.MEMBER);
                                    case "CO_OWNER":
                                        if (tp.getPosition() == GroupPosition.CO_OWNER) {
                                            c.reply(LangMessages.PLAYER_ALREADY_IN_POSITION.getMessage().replace("{position}", tp.getPosition().name()));
                                            return;
                                        }
                                        tp.setPosition(GroupPosition.CO_OWNER);
                                    case "OWNER":
                                        if (teamPlayer.getPosition() == GroupPosition.CO_OWNER) {
                                            c.reply(LangMessages.POSITION_TOO_LOW.getMessage().replace("{position}", GroupPosition.OWNER.name()));
                                            return;
                                        }
                                        if (tp.getPosition() == GroupPosition.OWNER) {
                                            c.reply(LangMessages.PLAYER_ALREADY_IN_POSITION.getMessage().replace("{position}", tp.getPosition().name()));
                                            return;
                                        }
                                        tp.setPosition(GroupPosition.OWNER);
                                }
                            }
                            c.reply(help());
                        }
                });
        this.command2 = Commands.create()
                .assertConsole()
                .handler(c -> {
                    if(c.args().size() > 1) {
                        GroupPlayerMap<TeamPlayer> groupPlayerMap = new GroupPlayerMap<>(TeamPlayer.class);
                        GroupMap<Team> groupMap = new GroupMap<>(Team.class);
                        if(groupMap.get(c.rawArg(1)) == null) {
                            c.reply("Team is null");
                            return;
                        }
                        if(Bukkit.getPlayerExact(c.rawArg(2)) == null) {
                            c.reply("Player is null");
                            return;
                        }
                        groupMap.get(c.rawArg(1)).getMembers().add(groupPlayerMap.get(Bukkit.getPlayerExact(c.rawArg(2))));
                    }
                });
    }
    public void register() {
        getCommand().register(commandNames);
        getCommand2().register("teamadmin");
    }

    private String help() {
        return "&6&l----&d&lTeams&6&l----&r\n&6&l/teams create <name> - Creates a team (Chat colors allowed)\n" +
                "&6&l/teams disband - Removes your current team if you're in one\n/teams join <name> - Requests to join a team\n" +
                "&6&l/teams sethome - Sets your teams home \n/teams home - Teleports you to the team home\n" +
                "&6&l/teams kick <member> - Kicks a member from the team\n/teams set <player> <MEMBER|CO_OWNER|OWNER>\n/teams invite <player> - Invite a player to your team\n/teams leave - Leave your current team\n" +
                "&6&l/teams who [team] - Shows the info of your team or another team\n /teams rename <newName> - Renames your team";
    }




}
