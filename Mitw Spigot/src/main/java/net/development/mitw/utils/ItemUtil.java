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
        ItemStack item = new ItemStack(id, amount, (byte)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createSkull(String owner, String name, String ... loreOptions) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (byte)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = loreOptions;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta((ItemMeta)meta);
        return item;
    }

    public static ItemStack createSkull(String owner, String name, Integer amount, String ... loreOptions) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (byte)3);
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(owner);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = loreOptions;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta((ItemMeta)meta);
        return item;
    }
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem1(int id, int amount, int data, String name, String ... lore) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = lore;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem1(Material mat, int amount, int data, String name, String ... lore) {
        ItemStack item = new ItemStack(mat, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = lore;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes('&', b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta(meta);
        return item;
    }
    
    
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem1(int id, int amount, int data, String name, List<String> lore) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }
    
    public static ItemStack createItem1(Material mat, int amount, int data, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', name));
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }

    
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem1Ench(int id, int amount, int data, String name, String ... lore) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<String>();
        String[] arrstring = lore;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String b = arrstring[n2];
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
            ++n2;
        }
        item.setItemMeta(meta);
        EnchantGlow.addGlow(item);
        return item;
    }

    
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem1Ench(int id, int amount, int data, String name, List<String> lore) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes((char)'&', (String)name));
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        EnchantGlow.addGlow(item);
        return item;
    }

    
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem2(int id, int amount, int data, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(id, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> color = new ArrayList<String>();
        for (String b : lore) {
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)b));
            meta.setLore(color);
        }
        item.setItemMeta(meta);
        return item;
    }

    
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem3(int id, int amount, int data, String name, String[] strings) {
		
        ItemStack item = new ItemStack(id, amount, (short)data);
        
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        ArrayList<String> color = new ArrayList<String>();
        
        for(String string : strings) {
            color.add(ChatColor.translateAlternateColorCodes((char)'&', (String)string));
            meta.setLore(color);
        }
        
        item.setItemMeta(meta);
        return item;
    }

	public static ItemStack createItem(Material material, String name) {
		ItemStack itemStack = new ItemStack(material);
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		itemStack.setItemMeta(meta);
		return itemStack;
	}
	
	public static ItemStack createItem(Material material, String name, int amount) {
		ItemStack item = new ItemStack(material, amount);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack createItem(Material material, String name, int amount, short damage) {
		ItemStack item = new ItemStack(material, amount, damage);
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}
	
	public static ItemStack renameItem(ItemStack item, String name) {
		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(name);
		item.setItemMeta(meta);

		return item;
	}

	public static ItemStack reloreItem(ItemStack item, String... lores) {
		return reloreItem(ReloreType.OVERWRITE, item, lores);
	}
	
	public static ItemStack reloreItem(ReloreType type, ItemStack item, String... lores) {
		ItemMeta meta = item.getItemMeta();

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
				List<String> nLore = new LinkedList<>(Arrays.asList(lores));
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
		OVERWRITE,
		PREPEND,
		APPEND
	}
}

class EnchantGlow extends EnchantmentWrapper {

	private static Enchantment glow;

    public EnchantGlow(int id) {
        super(id);
    }

    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    public EnchantmentTarget getItemTargetInstance() {
        return null;
    }

    public int getMaxLevel() {
        return 10;
    }

    public String getName() {
        return "Glow";
    }

    public int getStartLevel() {
        return 1;
    }

    public static Enchantment getGlow() {
        if (glow != null) {
            return glow;
        }
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        glow = new EnchantGlow(255);
        Enchantment.registerEnchantment((Enchantment)glow);
        return glow;
    }

    public static void addGlow(ItemStack item) {
        Enchantment glow = EnchantGlow.getGlow();
        item.addEnchantment(glow, 1);
    }
}

