package net.development.mitw.commands.cmds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.development.mitw.Mitw;
import net.development.mitw.chat.ChatDatabase;
import net.development.mitw.chat.check.Check;
import net.development.mitw.chat.check.CheckType;
import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import net.development.mitw.config.Settings;
import net.development.mitw.utils.Common;
import net.development.mitw.utils.ItemUtil;

public class ToxicCommand {

	public ToxicCommand() {
		Bukkit.getPluginManager().registerEvents(new MenuListener(), Mitw.getInstance());
	}

	@Command(names = {"toxic", "toxic help"}, permissionNode = "mitw.chat.admin")
	public static void help(final Player player) {
		Common.tell(player, "/toxic add <high/low/single> <word>", "/toxic remove <word>", "/toxic list");
	}

	@Command(names = {"toxic add"}, permissionNode = "mitw.chat.admin")
	public static void toxicAdd(final Player player, @Parameter(name = "high/low/single") final CheckType type, @Parameter(name = "word", wildcard = true) final String word) {
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
		final String toAdd = word.toLowerCase();
		db.putWords(toAdd, type);
		Check.getCheck(type.toString()).getCheckExams().add(toAdd);
		Common.tell(player, "&a成功新增單字&f " + toAdd + " &a進入辭典,等級為: &e" + type.toString());
	}

	@Command(names = {"toxic remove"}, permissionNode = "mitw.chat.admin")
	public static void toxicRemove(final Player player, @Parameter(name = "word", wildcard = true) final String word) {
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
		final String toRemove = word.toLowerCase();
		if (db.removeWords(toRemove)) {
			Common.tell(player, "&a成功從辭典移除單字&f " + toRemove);
		} else {
			Common.tell(player, "&c無法找到單字&f " + toRemove);
		}
	}

	@Command(names = {"toxic list"}, permissionNode = "mitw.chat.admin")
	public static void toxicList(final Player player) {
		new MainMenu().open(player);
	}

	@Command(names = {"toxic backup"}, permissionNode = "mitw.chat.admin")
	public static void toxicBackup(final Player player) {
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
		for (final String str : Settings.CHECK_HIGH) {
			db.putWords(str.toLowerCase(), CheckType.HIGH);
		}
		for (final String str : Settings.CHECK_LOW) {
			db.putWords(str.toLowerCase(), CheckType.LOW);
		}
		for (final String str : Settings.CHECK_SINGLE) {
			db.putWords(str.toLowerCase(), CheckType.SINGLE);
		}
		Common.tell(player, "done!");
	}

}

abstract class Menu {
	private static List<Menu> menuList = new ArrayList<>();
	private final Inventory inv;

	public Menu(final String title, final int raws) {
		inv = Bukkit.createInventory(null, raws * 9, Common.colored(title));
		menuList.add(this);
	}

	public Inventory getInv() {
		return inv;
	}

	public void open(final Player p) {
		p.openInventory(inv);
	}

	public void s(final int i, final ItemStack item) {
		inv.setItem(i, item);
	}

	public abstract void onClick(Player p, ItemStack i, ClickType action);

	public static List<Menu> getMenuList() {
		return menuList;
	}

	public static Menu getMenu(final Inventory i) {
		for (final Menu m : menuList)
			if (m.getInv().equals(i))
				return m;
		return null;
	}

}

class MainMenu extends Menu {
	ChatDatabase db;

	public MainMenu() {
		super("&c&l待審核單字", 6);
		db = Mitw.getInstance().getChatManager().getChatDB();
		int count = 0;
		for (final String str : db.getTop54TempWords()) {
			s(count, ItemUtil.createItem1(Material.PAPER, 1, 0, str, "", "&a左鍵來新增至辭典", "&c右鍵移除此文字"));
			count++;
		}
	}

	@Override
	public void onClick(final Player p, final ItemStack i, final ClickType action) {
		final String word = i.getItemMeta().getDisplayName().toLowerCase();
		if (action == ClickType.RIGHT) {
			db.removeTempWord(word);
			getInv().remove(i);
		}
		else if (action == ClickType.LEFT) {
			p.closeInventory();
			new TypeMenu(word).open(p);
		}
	}

}

class TypeMenu extends Menu {
	private static ItemStack high, low, single;
	static {
		high = ItemUtil.createItem1(Material.WOOL, 1, 14, "&f設定為: &c嚴重單字&7(High)", "&7如果說了會被禁言3小時", "&7打字內容包含就算");
		low = ItemUtil.createItem1(Material.WOOL, 1, 4, "&f設定為: &e輕微單字&7(Low)", "&7如果說了會被禁言1小時", "&7打字內容包含就算", "&7如果是指明玩家,且該玩名子包含這個單字", "&7將不會被禁言");
		single = ItemUtil.createItem1(Material.WOOL, 1, 3, "&f設定為: &c文字單字&7(Single)", "&7如果說了會被禁言3小時", "&7打字內容要跟單字'一摸一樣'");
	}
	private final String word;

	public TypeMenu(final String word) {
		super("&e&l處理動作為?", 3);
		s(11, high);
		s(13, low);
		s(15, single);
		this.word = word;
	}

	@Override
	public void onClick(final Player p, final ItemStack i, final ClickType action) {
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
		db.removeTempWord(word);
		if (i.equals(high)) {
			db.putWords(word, CheckType.HIGH);
			Check.getCheck("high").getCheckExams().add(word);
			Common.tell(p, "&a成功新增單字&f " + word + " &a進入辭典,等級為: &eHIGH");
		} else if (i.equals(low)) {
			db.putWords(word, CheckType.LOW);
			Check.getCheck("low").getCheckExams().add(word);
			Common.tell(p, "&a成功新增單字&f " + word + " &a進入辭典,等級為: &eLOW");
		} else if (i.equals(single)) {
			db.putWords(word, CheckType.SINGLE);
			Check.getCheck("single").getCheckExams().add(word);
			Common.tell(p, "&a成功新增單字&f " + word + " &a進入辭典,等級為: &bSINGLE");
		}
		p.closeInventory();
		new MainMenu().open(p);
	}

}

class MenuListener implements Listener {
	@EventHandler
	public void onInventoryClick(final InventoryClickEvent e) {
		final Menu m = Menu.getMenu(e.getInventory());
		if (m == null)
			return;
		e.setCancelled(true);
		final ItemStack i = e.getCurrentItem();
		if (i != null && i.getType() != Material.AIR && m.getInv().contains(i)) {
			m.onClick((Player) e.getWhoClicked(), e.getCurrentItem(), e.getClick());
		}
	}

	@EventHandler
	public void onInventoryClose(final InventoryCloseEvent e) {
		final Menu m = Menu.getMenu(e.getInventory());
		if (m == null)
			return;
		Menu.getMenuList().remove(m);
	}
}
