package me.sysdm.net.events;

import me.sysdm.net.groups.chat.ChatEvents;

public class EventManager {


    public static void setupEvents() {
        TeamEvents.onJoin();
        ChatEvents.onChat();
        
    }



}
