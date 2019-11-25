package mitw.bungee.commands;

import com.imaginarycode.minecraft.redisbungee.RedisBungee;
import mitw.bungee.Mitw;
import mitw.bungee.json.JsonChain;
import mitw.bungee.util.Common;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;
import java.util.Iterator;

public class Alert extends Command {

    public Alert() {
        super("alert", "mitw.admin");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (args.length == 0) {
            Common.tell(commandSender, "§c/alert <message>");
            return;
        }

        StringBuilder builder = new StringBuilder();

        Iterator<String> iterator = Arrays.stream(args).iterator();

        while (iterator.hasNext()) {
            builder.append(iterator.next());
            if (iterator.hasNext()) {
                builder.append(" ");
            }
        }

        Mitw.INSTANCE.getMitwJedis().write("BUNGEE_ALERT", new JsonChain().addProperty("message", builder.toString()).get());
    }
}
