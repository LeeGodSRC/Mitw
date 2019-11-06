package mitw.bungee.queue.module.util;

import net.md_5.bungee.api.ChatColor;

public class C
{
    public static final String BLACK;
    public static final String DBLUE;
    public static final String DGREEN;
    public static final String DAQUA;
    public static final String DRED;
    public static final String DPURPLE;
    public static final String GOLD;
    public static final String GRAY;
    public static final String DGRAY;
    public static final String BLUE;
    public static final String GREEN;
    public static final String AQUA;
    public static final String RED;
    public static final String LPURPLE;
    public static final String YELLOW;
    public static final String WHITE;
    public static final String MAGIC;
    public static final String PURPLE;
    public static final String CYAN;
    public static final String ORANGE;
    public static final String R;
    public static final String B;
    public static final String S;
    public static final String U;
    public static final String I;
    
    static {
        BLACK = ChatColor.BLACK.toString();
        DBLUE = ChatColor.DARK_BLUE.toString();
        DGREEN = ChatColor.DARK_GREEN.toString();
        DAQUA = ChatColor.DARK_AQUA.toString();
        DRED = ChatColor.DARK_RED.toString();
        DPURPLE = ChatColor.DARK_PURPLE.toString();
        GOLD = ChatColor.GOLD.toString();
        GRAY = ChatColor.GRAY.toString();
        DGRAY = ChatColor.DARK_GRAY.toString();
        BLUE = ChatColor.BLUE.toString();
        GREEN = ChatColor.GREEN.toString();
        AQUA = ChatColor.AQUA.toString();
        RED = ChatColor.RED.toString();
        LPURPLE = ChatColor.LIGHT_PURPLE.toString();
        YELLOW = ChatColor.YELLOW.toString();
        WHITE = ChatColor.WHITE.toString();
        MAGIC = ChatColor.MAGIC.toString();
        PURPLE = C.LPURPLE;
        CYAN = C.AQUA;
        ORANGE = C.GOLD;
        R = ChatColor.RESET.toString();
        B = ChatColor.BOLD.toString();
        S = ChatColor.STRIKETHROUGH.toString();
        U = ChatColor.UNDERLINE.toString();
        I = ChatColor.ITALIC.toString();
    }
    
    public static String translate(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
