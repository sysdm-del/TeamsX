package me.sysdm.net.events;

import me.sysdm.net.teams.chat.ChatEvents;

public class EventManager {


    public static void setupEvents() {
        TeamEvents.onJoin();
        ChatEvents.onChat();
        
    }



}
