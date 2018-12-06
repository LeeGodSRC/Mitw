package net.development.mitw.utils.timer;

import java.util.LinkedHashSet;
import java.util.Set;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Data;
import lombok.Getter;
import net.development.mitw.config.Configuration;

@Data
public class TimerManager implements Listener {

	@Getter
	private final Set<Timer> timers = new LinkedHashSet<>();

	private Configuration configuration;
	private final JavaPlugin plugin;

	public TimerManager(final JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void registerTimer(final Timer timer) {
		this.timers.add(timer);
		if (timer instanceof Listener) {
			this.plugin.getServer().getPluginManager().registerEvents((Listener) timer, this.plugin);
		}
	}

	public void unregisterTimer(final Timer timer) {
		this.timers.remove(timer);
	}

	@SuppressWarnings("unchecked")
	public <T extends Timer> T getTimer(final Class<T> timerClass) {
		for (final Timer timer : this.timers) {
			if (timer.getClass().equals(timerClass))
				return (T) timer;
		}

		return null;
	}

	public void saveTimerData() {
		for (final Timer timer : timers) {
			timer.save(configuration);
		}
		configuration.save();
	}

	public void loadTimerData() {
		configuration = new Configuration("timers");
		for (final Timer timer : timers) {
			timer.load(configuration);
		}
	}
}
