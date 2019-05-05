package net.development.mitw.menu.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.menu.Menu;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MenuUpdateTask implements Runnable {

	private long autoCloseMillis = TimeUnit.SECONDS.toMillis(30L);

	@Override
	public void run() {
		new HashMap<>(Menu.currentlyOpenedMenus).forEach((uuid, menu) -> {
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null)
				return;
			if (menu.isAutoUpdate()) {
				menu.openMenu(player, true);
			}
			if (menu.isAutoClose() && System.currentTimeMillis() - menu.getOpenMillis() > autoCloseMillis) {
				player.closeInventory();
			}
		});
	}

}
