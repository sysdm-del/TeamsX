package me.sysdm.net.teams;

import lombok.Getter;
import me.sysdm.net.lang.LangMessages;
import me.sysdm.net.teams.players.TeamPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Messenger {

    @Getter
    private final Set<String> invites = new HashSet<>();

    @Getter
    private final Set<String> requests = new HashSet<>();

    @Getter
    private final TeamPlayer player;

    @Getter
    private final UUID UUID;


    public Messenger(TeamPlayer player) {
        this.player = player;
        this.UUID = player.getUUID();
    }

    public boolean hasPendingInvite() {
        return invites.isEmpty();
    }

    public boolean hasPendingRequest() {
        return requests.isEmpty();
    }

    public void sendInvite(Messenger messenger) {
        messenger.getInvites().add(inviteMessage(player.getTeam(), player.getPlayer(), this));
    }

    public void sendRequest(Messenger messenger) {
        messenger.getInvites().add(requestMessage(player.getTeam(), player.getPlayer(), this));
    }

    private String requestMessage(Team team, Player requester, Messenger messenger) {
        return new TeamMessage("&a&l[!] " + requester.getName() + " wants to join you team!" + team.getTeamName() + " !" + " Click")
                .then("&6&l[here]").command("/teams adminjoin " + team.getTeamName() + " " + messenger.getPlayer().getPlayer().getName()).then(" &a&lto accept the invite.").getFormattedMessage();
    }

    private String inviteMessage(Team team, Player inviter, Messenger messenger) {
        StringBuilder members = new StringBuilder();
        for (TeamPlayer teamPlayer : team.getMembers()) {
            members.append(teamPlayer.getPlayer().getName()).append("\n");
        }
        return new TeamMessage("&a&l[!] You have been invited by " + inviter.getName() + " to join their team " + team.getTeamName() + " !" + " Click")
                .then("&6&l[here]").tooltip("&bTeam: " + team.getTeamName() + "\nOwner: " + team.getOwner().getPlayer().getName() +
                        "\nCoOwner: " + team.getCoOwner().getPlayer().getName() + "Members: \n" + members.toString()).command("/teams adminjoin " + team.getTeamName() + " " + messenger.getPlayer().getPlayer().getName()).then(" &a&l to join").getFormattedMessage();

    }
}