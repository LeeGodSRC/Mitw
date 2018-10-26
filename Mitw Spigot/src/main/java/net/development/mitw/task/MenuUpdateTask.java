package net.development.mitw.task;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.menu.Menu;

public class MenuUpdateTask implements Runnable {

	@Override
	public void run() {
		Menu.currentlyOpenedMenus.forEach((uuid, menu) -> {
			final Player player = Bukkit.getPlayer(uuid);
			if (player == null)
				return;
			menu.openMenu(player, true);
		});
	}

}
