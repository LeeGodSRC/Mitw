package mitw.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Memory extends Command {

    public Memory() {
        super("memory", "mitw.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length > 0 && strings[0].equalsIgnoreCase("gc")) {
            System.gc();
            commandSender.sendMessage("§e§lGC Success");
        }
        Runtime runtime = Runtime.getRuntime();
        double usedMemory = runtime.totalMemory() - runtime.freeMemory();
        double freeMemory = runtime.maxMemory() - usedMemory;
        commandSender.sendMessage("§eMemory: " + formatMem(usedMemory) + "/" + formatMem(runtime.maxMemory()) + " §7(" + formatMem(freeMemory) + " FREE)");
    }

    private static String formatMem(double mem) {
        return "§c" + Math.round(mem / 1024 / 1024) + "MB";
    }
}
