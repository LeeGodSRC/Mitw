package net.development.mitw.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.development.mitw.Mitw;

public class ItemUtil {

	@SuppressWarnings("deprecation")
	public static ItemStack createItem(final int id, final int amount, final int data, final String name) {
		final ItemStack item = new ItemStack(id, amount, (byte) data);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createSkull(final String owner, final String name, final Integer amount,
			final List<String> loreOptions) {
		final ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte) 3);
		final SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(owner);
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		final ArrayList<String> color = new ArrayList<>();
		final int n = loreOptions.size();
		int n2 = 0;
		while (n2 < n) {
			color.add(ChatColor.translateAlternateColorCodes('&', loreOptions.get(n2)));
			meta.setLore(color);
			++n2;
		}
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createSkull(final String owner, final String name, final Integer amount,
			final String... loreOptions) {
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
	public static ItemStack createItem1(final int id, final int amount, final int data, final String name,
			final String... lore) {
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

	public static ItemStack createItem1(final Material mat, final int amount, final int data, final String name,
			final String... lore) {
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
	public static ItemStack createItem1(final int id, final int amount, final int data, final String name,
			final List<String> lore) {
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

	public static ItemStack createItem1(final Material mat, final int amount, final int data, final String name,
			final List<String> lore) {
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

	public static ItemStack createEnch(final Material mat, final int amount, final int data, final Enchantment ench1,
			final int level1) {
		final ItemStack item = new ItemStack(mat, amount, (short) data);
		item.addUnsafeEnchantment(ench1, level1);
		return item;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack createItem1Ench(final int id, final int amount, final int data, final String name,
			final String... lore) {
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
	public static ItemStack createItem1Ench(final int id, final int amount, final int data, final String name,
			final List<String> lore) {
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
	public static ItemStack createItem2(final int id, final int amount, final int data, final String name,
			final ArrayList<String> lore) {
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
	public static ItemStack createItem3(final int id, final int amount, final int data, final String name,
			final String[] strings) {

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

	public static ItemStack createItem(final Material material, final String name) {
		final ItemStack itemStack = new ItemStack(material);
		final ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static ItemStack createItem(final Material material, final String name, final int amount) {
		final ItemStack item = new ItemStack(material, amount);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(final Material material, final String name, final int amount,
			final short damage) {
		final ItemStack item = new ItemStack(material, amount, damage);
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack renameItem(final ItemStack item, final String name) {
		final ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack reloreItem(final ItemStack item, final String... lores) {
		return reloreItem(ReloreType.OVERWRITE, item, lores);
	}

	public static ItemStack reloreItem(final ReloreType type, final ItemStack item, final String... lores) {
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

	private static final Map<String, ItemData> NAME_MAP = new HashMap<>();

	public static ItemData[] repeat(final Material material, final int times) {
		return repeat(material, (byte) 0, times);
	}

	public static ItemData[] repeat(final Material material, final byte data, final int times) {
		final ItemData[] itemData = new ItemData[times];

		for (int i = 0; i < times; i++) {
			itemData[i] = new ItemData(material, data);
		}

		return itemData;

	}

	public static ItemData[] armorOf(final ArmorPart part) {
		final List<ItemData> data = new ArrayList<>();

		for (final ArmorType at : ArmorType.values()) {
			data.add(new ItemData(Material.valueOf(at.name() + "_" + part.name()), (short) 0));
		}

		return data.toArray(new ItemData[data.size()]);
	}

	public static ItemData[] swords() {
		final List<ItemData> data = new ArrayList<>();

		for (final SwordType at : SwordType.values()) {
			data.add(new ItemData(Material.valueOf(at.name() + "_SWORD"), (short) 0));
		}

		return data.toArray(new ItemData[data.size()]);
	}

	public static void load() {
		NAME_MAP.clear();

		final List<String> lines = readLines();

		for (final String line : lines) {
			final String[] parts = line.split(",");

			NAME_MAP.put(parts[0],
					new ItemData(Material.getMaterial(Integer.parseInt(parts[1])), Short.parseShort(parts[2])));
		}
	}

	public static ItemStack get(final String input, final int amount) {
		final ItemStack item = get(input);

		if (item != null) {
			item.setAmount(amount);
		}

		return item;
	}

	public static ItemStack get(final String input) {
		if (NumberUtil.isInteger(input))
			return new ItemStack(Material.getMaterial(Integer.parseInt(input)));

		if (input.contains(":")) {
			if (NumberUtil.isShort(input.split(":")[1])) {
				if (NumberUtil.isInteger(input.split(":")[0]))
					return new ItemStack(Material.getMaterial(Integer.parseInt(input.split(":")[0])), 1,
							Short.parseShort(input.split(":")[1]));
				else {
					if (!NAME_MAP.containsKey(input.split(":")[0].toLowerCase()))
						return null;

					final ItemData data = NAME_MAP.get(input.split(":")[0].toLowerCase());
					return new ItemStack(data.getMaterial(), 1, Short.parseShort(input.split(":")[1]));
				}
			} else
				return null;
		}

		if (!NAME_MAP.containsKey(input))
			return null;

		return NAME_MAP.get(input).toItemStack();
	}

	public static String getName(final ItemStack item) {
		if (item.getDurability() != 0) {
			String reflectedName = BukkitReflection.getItemStackName(item);

			if (reflectedName != null) {
				if (reflectedName.contains(".")) {
					reflectedName = WordUtils.capitalize(item.getType().toString().toLowerCase().replace("_", " "));
				}

				return reflectedName;
			}
		}

		final String string = item.getType().toString().replace("_", " ");
		final char[] chars = string.toLowerCase().toCharArray();
		boolean found = false;

		for (int i = 0; i < chars.length; i++) {
			if (!found && Character.isLetter(chars[i])) {
				chars[i] = Character.toUpperCase(chars[i]);
				found = true;
			} else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') {
				found = false;
			}
		}

		return String.valueOf(chars);
	}

	private static List<String> readLines() {
		try {
			return IOUtils.readLines(Mitw.class.getClassLoader().getResourceAsStream("items.csv"));
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public enum ArmorPart {
		HELMET, CHESTPLATE, LEGGINGS, BOOTS
	}

	public enum ArmorType {
		DIAMOND, IRON, GOLD, LEATHER
	}

	public enum SwordType {
		DIAMOND, IRON, GOLD, STONE
	}

	@Getter
	@AllArgsConstructor
	public static class ItemData {

		private final Material material;
		private final short data;

		public String getName() {
			return ItemUtil.getName(toItemStack());
		}

		public boolean matches(final ItemStack item) {
			return item != null && item.getType() == material && item.getDurability() == data;
		}

		public ItemStack toItemStack() {
			return new ItemStack(material, 1, data);
		}

	}
}

class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

	public EnchantGlow(final int id) {
		super(id);
	}

	@Override
	public boolean canEnchantItem(final ItemStack item) {
		return true;
	}

	@Override
	public boolean conflictsWith(final Enchantment other) {
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
		if (glow != null)
			return glow;
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

	public static void addGlow(final ItemStack item) {
		final Enchantment glow = EnchantGlow.getGlow();
		item.addEnchantment(glow, 1);
	}

}
