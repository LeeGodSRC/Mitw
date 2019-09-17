package net.development.mitw.commands.cmds;

import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class RankCommand {

    @Command(names = {"rank"}, permissionNode = "mitw.admin")
    public static void execute(CommandSender sender, @Parameter(name = "Sub command") String sub, @Parameter(name = "Sub command 2") String sub2, @Parameter(name = "Sub command 3") String sub3) {
        switch (sub.toLowerCase()) {
            case "buy":

                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + sub2 + " parent set " + sub3);

                Bukkit.broadcast("§a§l玩家 " + sub2 + " 購買了 " + sub3, "mitw.admin");
                break;
            default:
                sender.sendMessage("Unknown command.");
                break;
        }
    }

}
