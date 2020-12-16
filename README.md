# TeamsXAPI
TeamsXAPI is an API for Spigot developers.

### Installation
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
@Override
    public GroupPlayer getOwner() {
        return null;
    }

    @Override
    public Set<GroupPlayer> getMembers() {
        return null;
    }

    @Override
    public Location getHomeLocation() {
        return null;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public GroupPlayer getCoOwner() {
        return null;
    }

    @Override
    public void setOwner(GroupPlayer groupPlayer) {

    }

    @Override
    public void setCoOwner(GroupPlayer groupPlayer) {

    }

    @Override
    public void setHomeLocation(Location location) {

    }

    @Override
    public void setGroupName(String newName) {

    }

    @Override
    public boolean isInGroup(GroupPlayer groupPlayer) {
        return false;
    }
}```

