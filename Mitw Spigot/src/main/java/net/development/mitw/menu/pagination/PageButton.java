package net.development.mitw.menu.pagination;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AllArgsConstructor;
import net.development.mitw.menu.Button;

@AllArgsConstructor
public class PageButton extends Button {

	private final int mod;
	private final PaginatedMenu menu;

	@Override
	public ItemStack getButtonItem(final Player player) {
		final ItemStack itemStack = new ItemStack(Material.CARPET);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		if (this.hasNext(player)) {
			itemMeta.setDisplayName(this.mod > 0 ? ChatColor.GREEN + "Next page" : ChatColor.RED + "Previous page");
		} else {
			itemMeta.setDisplayName(ChatColor.GRAY + (this.mod > 0 ? "Last page" : "First page"));
		}

		itemMeta.setLore(Arrays.asList(
				"",
				ChatColor.YELLOW + "Right click to",
				ChatColor.YELLOW + "jump to a page"
				));

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	@Override
	public void clicked(final Player player, final int i, final ClickType clickType, final int hb) {
		if (clickType == ClickType.RIGHT) {
			new ViewAllPagesMenu(this.menu).openMenu(player);
			playNeutral(player);
		} else {
			if (hasNext(player)) {
				this.menu.modPage(player, this.mod);
				Button.playNeutral(player);
			} else {
				Button.playFail(player);
			}
		}
	}

	private boolean hasNext(final Player player) {
		final int pg = this.menu.getPage() + this.mod;
		return pg > 0 && this.menu.getPages(player) >= pg;
	}

}
