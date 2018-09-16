package net.development.mitw.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUtils {

	public static void runAsync(Runnable runnable) {
		ExecutorService executor = Executors.newSingleThreadExecutor(
				new ThreadFactoryBuilder().setNameFormat("UltimateUHC-" + runnable.hashCode()).build());

		executor.submit(runnable);
	}
}