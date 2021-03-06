package me.sysdm.net.groups;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GroupMessage {

    private final List<TextComponent> components = new ArrayList<>();


    public GroupMessage(String message) {
        components.add(new TextComponent(color(message)));
    }


    public GroupMessage suggest(String command) {
        this.getLatestComponent().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }


    public GroupMessage command(String command) {
        this.getLatestComponent().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }


    public GroupMessage tooltip(String... text) {
        ComponentBuilder componentBuilder = new ComponentBuilder(color(text[0]));
        int i = 0;
        if (text.length > 1) {

            for (String s : text) {
                ++i;
                if (i > 1) {
                    componentBuilder.append("\n" + color(s));
                }
            }
        }
        this.getLatestComponent().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create()));
        return this;
    }



    public GroupMessage tooltip(Collection<String> text) {
        ComponentBuilder componentBuilder = new ComponentBuilder(color(text.stream().findFirst().get()));
        int i = 0;
        if (text.size() > 1) {
            for (String s : text) {
                ++i;
                if (i > 1) {
                    componentBuilder.append("\n" + color(s));
                }
            }
        }
        this.getLatestComponent().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create()));
        return this;
    }



    public GroupMessage link(String link) {
        this.getLatestComponent().setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, link));
        return this;
    }



    public GroupMessage then(String message) {
        components.add(new TextComponent(color(message)));
        return this;
    }



    private TextComponent getLatestComponent() {
        return components.get(components.size() - 1);
    }




    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        components.forEach(component -> sb.append(" ").append(component.getText()));
        return sb.toString();
    }


    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
