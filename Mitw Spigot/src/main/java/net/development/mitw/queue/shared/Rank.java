package net.development.mitw.queue.shared;

import java.util.ArrayList;
import java.util.List;

public class Rank
{
    private static List<Rank> ranks;
    private String name;
    private String permission;
    private int priority;
    
    static {
        Rank.ranks = new ArrayList<Rank>();
    }
    
    public Rank(final String name, final String permission, final int priority) {
        this.name = name;
        this.permission = permission;
        this.priority = priority;
        Rank.ranks.add(this);
    }
    
    public static Rank getRank(final String permission) {
        return Rank.ranks.stream().filter(r -> r.getPermission().equalsIgnoreCase(permission)).findAny().orElse(null);
    }
    
    @Override
    public String toString() {
        return this.permission;
    }
    
    public static List<Rank> getRanks() {
        return Rank.ranks;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public int getPriority() {
        return this.priority;
    }
}
