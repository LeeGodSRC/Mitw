package net.development.mitw.language;

import net.development.mitw.player.MitwPlayer;
import net.development.mitw.utils.RV;
import net.development.mitw.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractLanguageAPI {

    public void send(final Player p, final String translateMessage, final RV... replaceValue) {
        p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue));
    }

    public void send(final String translateMessage, final RV... replaceValue) {
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
    }

    public void send(final Collection<Player> player, final String translateMessage, final RV... replaceValue) {
        player.forEach(p -> p.sendMessage(StringUtil.replace(translate(p, translateMessage), replaceValue)));
    }

    public void send2(final Collection<UUID> player, final String translateMessage, final RV... replaceValue) {
        send(player.stream().map(Bukkit::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()), translateMessage, replaceValue);
    }

    public String translate(Player player, String format, RV... rvs) {
        return StringUtil.replace(this.translate(MitwPlayer.getByUuid(player.getUniqueId()), format), rvs);
    }

    public List<String> translateArrays(Player player, String format, RV... rvs) {
        return this.translateArrays(MitwPlayer.getByUuid(player.getUniqueId()), format).stream().map(string -> StringUtil.replace(string, rvs)).collect(Collectors.toList());
    }

    public String translate(MitwPlayer player, String format, RV... rvs) {
        return StringUtil.replace(this.translate(player, format), rvs);
    }

    public List<String> translateArrays(MitwPlayer player, String format, RV... rvs) {
        return this.translateArrays(player, format).stream().map(string -> StringUtil.replace(string, rvs)).collect(Collectors.toList());
    }

    public abstract String translate(MitwPlayer mitwPlayer, String format);

    public abstract List<String> translateArrays(MitwPlayer mitwPlayer, String format);

}
