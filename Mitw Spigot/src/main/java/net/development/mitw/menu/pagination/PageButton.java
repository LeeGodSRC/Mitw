package net.development.mitw.menu.pagination;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.AllArgsConstructor;
import net.development.mitw.Mitw;
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
			itemMeta.setDisplayName(this.mod > 0 ? ChatColor.GREEN +
					Mitw.getInstance().getCoreLanguage().translate(player, "nextPage")
					: ChatColor.RED + Mitw.getInstance().getCoreLanguage().translate(player, "previousPage"));

			itemMeta.setLore(Mitw.getInstance().getCoreLanguage().translateArrays(player, "jumpToPage_lore"));

		} else {

			itemMeta.setDisplayName(ChatColor.GRAY + (this.mod > 0 ?
					Mitw.getInstance().getCoreLanguage().translate(player, "lastPage") :
						Mitw.getInstance().getCoreLanguage().translate(player, "firstPage")));

			itemMeta.setLore(Mitw.getInstance().getCoreLanguage().translateArrays(player, "chosePage_lore"));

		}

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
