package net.development.mitw.chat;

import net.development.mitw.config.Configuration;

import java.util.UUID;

public class PlayerCache {
	private String lastMessage = "";
	private long lastTalkTime = 0;

	public PlayerCache(final UUID u) {
	}

	public long getLastTalkTime() {
		return lastTalkTime;
	}

	public void setLastTalkTime(final long lastTalkTime) {
		this.lastTalkTime = lastTalkTime;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(final String lastMessage) {
		this.lastMessage = lastMessage;
	}

}
