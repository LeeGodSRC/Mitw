package mitw.bungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Memory extends Command {

    public Memory() {
        super("memory", "mitw.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Runtime rt = Runtime.getRuntime();
        commandSender.sendMessage("§eMemory: " + ((int) ((rt.maxMemory() - rt.totalMemory() + rt.freeMemory()) / 1048576))+ "mb/" + ((int) (rt.maxMemory() / 1048576)) + "mb §7(" + ((int)(rt.freeMemory() / 1048576)) + "mb)");
    }
}
