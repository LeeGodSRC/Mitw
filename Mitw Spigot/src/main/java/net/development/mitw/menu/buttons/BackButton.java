package net.development.mitw.menu.buttons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AllArgsConstructor;
import net.development.mitw.Mitw;
import net.development.mitw.menu.Button;
import net.development.mitw.menu.Menu;

@AllArgsConstructor
public class BackButton extends Button {

	private final Menu back;

	@Override
	public ItemStack getButtonItem(final Player player) {
		final ItemStack itemStack = new ItemStack(Material.BED);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(ChatColor.RED + Mitw.getInstance().getCoreLanguage().translate(player, "goBack"));
		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	@Override
	public void clicked(final Player player, final int i, final ClickType clickType, final int hb) {
		Button.playNeutral(player);

		this.back.openMenu(player);
	}

}
