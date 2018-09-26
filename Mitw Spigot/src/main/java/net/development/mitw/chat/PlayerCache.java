package net.development.mitw.chat;

import java.util.UUID;

import net.development.mitw.config.SimpleConfig;

public class PlayerCache {
	private final SimpleConfig cfg = new SimpleConfig("cache.dat", false);;
	private String lastMessage = "";
	private long lastTalkTime = 0;
	private boolean isMute = false;
	private String suffix = "";
	private final String path;

	public PlayerCache(UUID u) {
		onLoad();
		path = u.toString();
	}
	private void onLoad() {
		suffix = cfg.getString(path + ".suffix", "");
		isMute = cfg.getBoolean(path + ".is-mute", false);
		lastMessage = cfg.getString(path + ".last-chat-msg", "");
		lastTalkTime = cfg.getLong(path + ".last-talk-time", 0L);
	}

	public void save() {
		cfg.set(path + ".suffix", suffix);
		cfg.set(path + ".is-mute", isMute);
		cfg.set(path + ".last-chat-msg", lastMessage);
		cfg.set(path + ".last-talk-time", lastTalkTime);
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
