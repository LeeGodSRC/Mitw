package net.development.mitw.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.development.mitw.Mitw;
import net.development.mitw.chat.ChatDatabase;
import net.development.mitw.chat.ChatManager;
import net.development.mitw.chat.check.Check;
import net.development.mitw.chat.check.CheckType;
import net.development.mitw.utils.Common;
import net.development.mitw.utils.ItemUtil;

public class ToxicCommand extends Command {

	public ToxicCommand() {
		super("toxic");
		Bukkit.getPluginManager().registerEvents(new MenuListener(), Mitw.getInstance());
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(!sender.hasPermission("mitw.chat.admin"))
			return false;
		if(args.length < 1) {
			help(sender);
			return false;
		}
		final String arg = args[0].toLowerCase();
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
		switch (args[0].toLowerCase()) {
		case "add":
			if(args.length < 3) {
				Common.tell(sender, "&7Usage: /toxic add <word> <high/low/single>");
				return false;
			}
			final CheckType type = CheckType.valueOf(args[2]);
			db.putWords(arg, type);
			Common.tell(sender, "&a成功新增單字&f " + arg + " &a進入辭典,等級為: &e" + type.toString());
			break;
		case "remove":
			if(args.length < 2) {
				Common.tell(sender, "&7Usage: /toxic remove <word>");
				return false;
			}
			if(db.removeWords(args[1].toLowerCase()))
				Common.tell(sender, "&a成功從辭典移除單字&f " + arg);
			else
				Common.tell(sender, "&c無法找到單字&f " + arg);
			break;
		case "list":
			if(!(sender instanceof Player))
				return false;
			new MainMenu().open((Player) sender);
			break;

		default:
			break;
		}
		return false;
	}

	public void help(CommandSender p) {
		Common.tell(p,
				"/toxic add <word> <high/low/single>",
				"/toxic remove <word>",
				"/toxic list");
	}

}

abstract class Menu {
	private static List<Menu> menuList = new ArrayList<>();
	private final Inventory inv;

	public Menu(String title, int raws) {
		inv = Bukkit.createInventory(null, raws * 9, Common.colored(title));
		menuList.add(this);
	}

	public Inventory getInv() {
		return inv;
	}

	public void open(Player p) {
		p.openInventory(inv);
	}

	public void s(int i, ItemStack item) {
		inv.setItem(i, item);
	}

	public abstract void onClick(Player p, ItemStack i);

	public static List<Menu> getMenuList() {
		return menuList;
	}

	public static Menu getMenu(Inventory i) {
		for (final Menu m : menuList)
			if (m.getInv().equals(i))
				return m;
		return null;
	}

}

class MainMenu extends Menu {

	public MainMenu() {
		super("&c&l待審核單字", 6);
		final ChatManager chm = Mitw.getInstance().getChatManager();
		for (int a = 0; a < 54; a++) {
			s(a, ItemUtil.createItem1(Material.PAPER, 1, 0, "DB_STRING", "","&a左鍵來新增至辭典","&c右鍵移除此文字"));
		}
	}

	@Override
	public void onClick(Player p, ItemStack i) {
		new TypeMenu(i.getItemMeta().getDisplayName().toLowerCase()).open(p);
	}

}

class TypeMenu extends Menu {
	private static ItemStack high,low,single;
	static {
		high = ItemUtil.createItem1(Material.WOOL, 1, 14, "&f設定為: &c嚴重單字&7(High)",
				"&7如果說了會被禁言3小時",
				"&7打字內容包含就算");
		low = ItemUtil.createItem1(Material.WOOL, 1, 4, "&f設定為: &e輕微單字&7(Low)",
				"&7如果說了會被禁言1小時",
				"&7打字內容包含就算",
				"&7如果是指明玩家,且該玩名子包含這個單字",
				"&7將不會被禁言");
		single = ItemUtil.createItem1(Material.WOOL, 1, 3, "&f設定為: &c文字單字&7(Single)",
				"&7如果說了會被禁言3小時",
				"&7打字內容要跟單字'一摸一樣'");
	}
	private final String word;

	public TypeMenu(String word) {
		super("&e&l處理動作為?", 3);
		s(11, high);
		s(13, low);
		s(15, single);
		this.word = word;
	}

	@Override
	public void onClick(Player p, ItemStack i) {
		final ChatDatabase db = Mitw.getInstance().getChatManager().getChatDB();
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
	}

}

class MenuListener implements Listener {
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		final Menu m = Menu.getMenu(e.getInventory());
		if (m == null)
			return;
		e.setCancelled(true);
		final ItemStack i = e.getCurrentItem();
		if (i != null && i.getType() != Material.AIR && m.getInv().contains(i)) {
			m.onClick((Player) e.getWhoClicked(), e.getCurrentItem());
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		final Menu m = Menu.getMenu(e.getInventory());
		if (m == null)
			return;
		Menu.getMenuList().remove(m);
	}
}
