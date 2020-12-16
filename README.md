# TeamsXAPI
TeamsXAPI is an API for Spigot developers.

### Installation
NOTE: Lombok highly recommended.
Maven repository:
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

Maven dependency:
```xml
<dependency>
    <groupId>com.github.sysdm-del</groupId>
    <artifactId>TeamsX</artifactId>
    <version>1.0</version>
</dependency>
```

### Usage

The "goal" with this API is to be able to make custom groups. To make a group, make a class called for example "MyGroup" that implements Group.
```java
public class MyGroup implements Group {
}```
Now, you can change all "options". Example of the `Group` class here:

https://github.com/sysdm-del/TeamsX/blob/sysdm/src/main/java/me/sysdm/net/groups/teams/Team.java

### Maps
Now, let's go through the maps.
There are four different maps: `GroupAndPlayerMap`, `GroupMap`, `GroupPlayerMap` and `MessengerMap`.

The first one is a map that contains all the groups and players (GroupPlayer's, will cover that soon).
The second one contains all the groups, the third one contains all the GroupPlayer's (again, i will cover that soon)
and the fourth one contains all the messengers (Will also cover that soon).
To create a map, do for example `GroupMap<MyOwnGroupPlayerClass> map = new GroupMap<>(MyOwnGroupPlayerClass.class);`

To access the map, do getMap(), but i highly recommend to use the `get()` method (not the hashmap one, but the actual class method.) to get things.

### GroupPlayer's
GroupPlayer is the player that is in your group.
You need to create a custom group player for your group, and to create one just implement `GroupPlayer` in your class.

Example of a GroupPlayer class here:

https://github.com/sysdm-del/TeamsX/blob/sysdm/src/main/java/me/sysdm/net/groups/teams/TeamPlayer.java

### Messenger's
And last but not least, messengers. The purpose of messengers is to send stuff to other group players.

All you have to do to create your own messenger, is to implement `Messenger`.

Example of a messenger class:

https://github.com/sysdm-del/TeamsX/blob/sysdm/src/main/java/me/sysdm/net/groups/teams/TeamMessenger.java

### Extra

### GroupMessage
This is a "extra" thing i made, it's almost like FancyMessage.

It's a thing to make more "fancy" messages.

Hope that you made some sense of this, i'm really bad at explaining.
Example of on how to make a "command" class:
NOTE: I use Lucko's helper for this.
https://github.com/sysdm-del/TeamsX/blob/sysdm/src/main/java/me/sysdm/net/groups/commands/TeamCommand.java




