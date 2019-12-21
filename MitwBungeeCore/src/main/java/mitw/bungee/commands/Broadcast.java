package mitw.bungee.commands;

import mitw.bungee.Mitw;
import mitw.bungee.config.impl.General;
import mitw.bungee.json.JsonChain;
import mitw.bungee.util.BroadcastUtil;
import mitw.bungee.util.Common;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class Broadcast extends Command {

    public Broadcast() {
        super("broadcast", "mitw.admin", "ba");
    }

    private String[] broadcasts = {"meetup", "uhc", "sg", "castlewars", "events"};

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            return;
        }

        String server = args[0];

        boolean found = false;

        for (String serverContain : broadcasts) {
            if (server.contains(serverContain)) {
                found = true;
                break;
            }
        }

        if (!found) {
            Common.tell(sender, "§cTarget server isn't found!");
            return;
        }

        if (args.length == 2) {
            if (!isBoolean(args[1])) {
                Common.tell(sender, "§cSecond arg should be boolean");
                return;
            }
            if (General.REDIS_BUNGEE) {
                Mitw.INSTANCE.getMitwJedis().write("BROADCAST_GAME", new JsonChain().addProperty("server", server).addProperty("extra", Boolean.parseBoolean(args[1])).get());
            } else {
                BroadcastUtil.broadcast(server, Boolean.parseBoolean(args[1]));
            }
        } else {
            if (General.REDIS_BUNGEE) {
                Mitw.INSTANCE.getMitwJedis().write("BROADCAST_GAME", new JsonChain().addProperty("server", server).get());
            } else {
                BroadcastUtil.broadcast(server, false);
            }
        }
        Common.tell(sender, "§a成功發送公告");
    }

    public boolean isBoolean(final String string) {
        try {
            Boolean.parseBoolean(string);
        } catch (final Exception e) {
            return false;
        }
        return true;
    }

}
