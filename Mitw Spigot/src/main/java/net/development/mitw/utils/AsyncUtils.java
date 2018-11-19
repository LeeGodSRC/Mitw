package net.development.mitw.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import net.development.mitw.Mitw;

public class AsyncUtils {

	public static void runAsync(final Runnable runnable) {
		final ExecutorService executor = Executors.newSingleThreadExecutor(
				new ThreadFactoryBuilder().setNameFormat("UltimateUHC-" + runnable.hashCode()).build());

		executor.submit(runnable);
	}

	public static BukkitTask runTaskTimer(final BukkitRunnable runnable, final long delay, final long time, final boolean async) {
		if (async)
			return runnable.runTaskTimerAsynchronously(Mitw.getInstance(), delay, time);
		return runnable.runTaskTimer(Mitw.getInstance(), delay, time);
	}

	public static BukkitTask runTaskLater(final Runnable runnable, final long delay, final boolean async) {
		if (async)
			return Bukkit.getScheduler().runTaskLaterAsynchronously(Mitw.getInstance(), runnable, delay);
		return Bukkit.getScheduler().runTaskLater(Mitw.getInstance(), runnable, delay);
	}
}