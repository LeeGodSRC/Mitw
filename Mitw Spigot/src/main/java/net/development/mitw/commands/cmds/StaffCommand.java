package net.development.mitw.commands.cmds;

import me.efla.API.API;
import me.efla.Main;
import me.efla.connection.Communication;
import net.development.mitw.Mitw;
import net.development.mitw.commands.Command;
import net.development.mitw.commands.param.Parameter;
import net.development.mitw.utils.PlayerUtil;
import net.development.mitw.utils.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class StaffCommand {

    private static final String FREEZE_TAG = "MitwFreezeTag";

    @Command(names = {"ss", "freeze"}, permissionNode = "mitw.admin")
    public static void freeze(Player player, @Parameter(name = "target") Player target) {
        if (target == null) {
            player.sendMessage(Mitw.getInstance().getCoreLanguage().translate(player, "targetNULL"));
            return;
        }
        if (target.hasMetadata(FREEZE_TAG)) {
            target.removeMetadata(FREEZE_TAG, Mitw.getInstance());
            PlayerUtil.allowMovement(target);
            if (Main.lista.contains(target.getName())) {
                Communication.removePlayer(target.getName());
            }
        } else {
            PlayerUtil.denyMovement(target);
            target.setMetadata(FREEZE_TAG, new FixedMetadataValue(Mitw.getInstance(), player.getName()));
            API.sendCheckRequest(player, true);
            for (String message : Mitw.getInstance().getCoreLanguage().translateArrays(target, "freezed")) {
                target.sendMessage(StringUtil.replace(message, "%0", player.getName()));
            }
        }
    }

}
