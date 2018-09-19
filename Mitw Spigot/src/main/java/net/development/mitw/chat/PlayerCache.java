package net.development.mitw.chat;

import java.util.UUID;

import net.development.mitw.config.SimpleConfig;

public class PlayerCache {
	private final SimpleConfig cfg;
	private String lastMessage = "";
	private long lastTalkTime = 0;
	private boolean isMute = false;
	private String suffix = "";

	public PlayerCache(UUID u) {
		cfg = new SimpleConfig("cache.dat", false);
		cfg.setPathPrefix(u.toString());
		onLoad();
	}

	private void onLoad() {
		suffix = cfg.getString("suffix", "");
		isMute = cfg.getBoolean("is-mute", false);
		lastMessage = cfg.getString("last-chat-msg", "");
		lastTalkTime = cfg.getLong("last-talk-time", 0L);
	}

	public void save() {
		cfg.set("suffix", suffix);
		cfg.set("is-mute", isMute);
		cfg.set("last-chat-msg", lastMessage);
		cfg.set("last-talk-time", lastTalkTime);
		cfg.saveConfig();
	}

	public long getLastTalkTime() {
		return lastTalkTime;
	}

	public void setLastTalkTime(long lastTalkTime) {
		this.lastTalkTime = lastTalkTime;
	}

	public String getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(String lastMessage) {
		this.lastMessage = lastMessage;
	}

	public boolean isMute() {
		return isMute;
	}

	public void setMute(boolean isMute) {
		this.isMute = isMute;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
