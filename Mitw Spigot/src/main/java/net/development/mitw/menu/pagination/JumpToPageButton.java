package net.development.mitw.menu.pagination;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.ilummc.tlib.util.Strings;

import lombok.AllArgsConstructor;
import net.development.mitw.Mitw;
import net.development.mitw.menu.Button;

@AllArgsConstructor
public class JumpToPageButton extends Button {

	private final int page;
	private final PaginatedMenu menu;
	private final boolean current;

	@Override
	public ItemStack getButtonItem(final Player player) {
		final ItemStack itemStack = new ItemStack(this.current ? Material.ENCHANTED_BOOK : Material.BOOK, this.page);
		final ItemMeta itemMeta = itemStack.getItemMeta();

		itemMeta.setDisplayName(ChatColor.YELLOW + Strings.replaceWithOrder(Mitw.getInstance().getCoreLanguage().translate(player, "page"), this.page + ""));

		if (this.current) {
			itemMeta.setLore(Arrays.asList(
					"",
					ChatColor.GREEN + Mitw.getInstance().getCoreLanguage().translate(player, "currentPage")
					));
		}

		itemStack.setItemMeta(itemMeta);

		return itemStack;
	}

	@Override
	public void clicked(final Player player, final int i, final ClickType clickType, final int hb) {
		this.menu.modPage(player, this.page - this.menu.getPage());
		Button.playNeutral(player);
	}

}
