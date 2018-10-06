package net.development.mitw.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtil {

	@SuppressWarnings("deprecation")
	public static ItemStack createItem(int id, int amount, int data, String name) {
		final ItemStack item = new ItemStack(id, amount, (byte) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createSkull(String owner, String name, String... loreOptions) {
		final ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		final String[] arrstring = loreOptions;
		final int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			final String b = arrstring[n2];
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
			++n2;
		}
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createSkull(String owner, String name, Integer amount, String... loreOptions) {
		final ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		final String[] arrstring = loreOptions;
		final int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			final String b = arrstring[n2];
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
			++n2;
		}
		item.setItemMeta(meta);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem1(int id, int amount, int data, String name, String... lore) {
		final ItemStack item = new ItemStack(id, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		final String[] arrstring = lore;
		final int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			final String b = arrstring[n2];
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
			++n2;
		}
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem1(Material mat, int amount, int data, String name, String... lore) {
		final ItemStack item = new ItemStack(mat, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		final String[] arrstring = lore;
		final int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			final String b = arrstring[n2];
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
			++n2;
		}
		item.setItemMeta(meta);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem1(int id, int amount, int data, String name, List<String> lore) {
		final ItemStack item = new ItemStack(id, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		for (final String b : lore) {
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
		}
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem1(Material mat, int amount, int data, String name, List<String> lore) {
		final ItemStack item = new ItemStack(mat, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		for (final String b : lore) {
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
		}
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createEnch(Material mat, int amount, int data, Enchantment ench1, int level1) {
		final ItemStack item = new ItemStack(mat, amount, (short) data);
		item.addUnsafeEnchantment(ench1, level1);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem1Ench(int id, int amount, int data, String name, String... lore) {
		final ItemStack item = new ItemStack(id, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		final String[] arrstring = lore;
		final int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			final String b = arrstring[n2];
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
			++n2;
		}
		item.setItemMeta(meta);
		EnchantGlow.addGlow(item);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem1Ench(int id, int amount, int data, String name, List<String> lore) {
		final ItemStack item = new ItemStack(id, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		for (final String b : lore) {
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
		}
		item.setItemMeta(meta);
		EnchantGlow.addGlow(item);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem2(int id, int amount, int data, String name, ArrayList<String> lore) {
		final ItemStack item = new ItemStack(id, amount, (short) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		final ArrayList<String> color = new ArrayList<>();
		for (final String b : lore) {
			color.add(ChatColor.translateAlternateColorCodes('&', b));
			meta.setLore(color);
		}
		item.setItemMeta(meta);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem3(int id, int amount, int data, String name, String[] strings) {

		final ItemStack item = new ItemStack(id, amount, (short) data);

		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		final ArrayList<String> color = new ArrayList<>();

		for (final String string : strings) {
			color.add(ChatColor.translateAlternateColorCodes('&', string));
			meta.setLore(color);
		}

		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material material, String name) {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack createItem(Material material, String name, int amount) {
		final ItemStack item = new ItemStack(material, amount);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(Material material, String name, int amount, short damage) {
		final ItemStack item = new ItemStack(material, amount, damage);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack renameItem(ItemStack item, String name) {
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack reloreItem(ItemStack item, String... lores) {
		return reloreItem(ReloreType.OVERWRITE, item, lores);
	}

	public static ItemStack reloreItem(ReloreType type, ItemStack item, String... lores) {
		final ItemMeta meta = item.getItemMeta();

		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new LinkedList<>();
		}

		switch (type) {
		case APPEND:
			lore.addAll(Arrays.asList(lores));
			meta.setLore(lore);
			break;
		case PREPEND:
			final List<String> nLore = new LinkedList<>(Arrays.asList(lores));
			nLore.addAll(lore);
			meta.setLore(nLore);
			break;
		case OVERWRITE:
			meta.setLore(Arrays.asList(lores));
			break;
		}

		item.setItemMeta(meta);
		return item;
	}

	public enum ReloreType {
		OVERWRITE, PREPEND, APPEND
	}
}

class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	public EnchantGlow(int id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(ItemStack item) {
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment other) {
		return false;
	}

	public EnchantmentTarget getItemTargetInstance() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return 10;
	}

	@Override
	public String getName() {
		return "Glow";
	}

	@Override
	public int getStartLevel() {
		return 1;
	}

	public static Enchantment getGlow() {
		if (glow != null) {
			return glow;
		}
		try {
			final Field f = Enchantment.class.getDeclaredField("acceptingNew");
			f.setAccessible(true);
			f.set(null, true);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		glow = new EnchantGlow(255);
		Enchantment.registerEnchantment(glow);
		return glow;
	}

	public static void addGlow(ItemStack item) {
		final Enchantment glow = EnchantGlow.getGlow();
		item.addEnchantment(glow, 1);
	}
}
