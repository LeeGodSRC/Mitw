package net.development.mitw.chat;

import org.bukkit.entity.Player;

public interface ChatHandler {
	
	public String getPrefix(Player p);
	
	public String getSuffix(Player p);

}
