package mitw.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mitw.Bungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class Report extends Command implements TabExecutor {
	public List<ProxiedPlayer> report = new ArrayList<>();

	public Report() {
		super("report", (String) null, new String[] { "MReport" });
	}

	@Override
	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof ProxiedPlayer) {
			final ProxiedPlayer p = (ProxiedPlayer) sender;
			if (args.length == 0) {
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&8[&6檢舉系統&8]&7 使用方法: &c/report <玩家名稱> <原因>"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&4&l可檢舉的事件:&7 組隊, 吵架, 外掛(KillAura, Reach, HitBox, Fly, AutoClicker...)"));
			} else if (args.length >= 2) {
				if (this.report.contains(p)) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6檢舉系統&8]&c 不要spam report指令喔!!"));
					return;
				}

				final StringBuilder sb = new StringBuilder("");
				final ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);

				for (int msg = 1; msg < args.length; ++msg) {
					sb.append(args[msg]).append(" ");
				}

				if (target == null) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6檢舉系統&8]&c 目標玩家不存在"));
					return;
				}
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6檢舉系統&8]&a 成功發送檢舉,請耐心等待處理...."));
				final String var7 = sb.toString();
				ProxyServer.getInstance().getPlayers().stream().filter((mod) -> {
					return mod.hasPermission("mitw.admin");
				}).forEach((mod) -> {
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------------------------"));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l檢舉系統提示 "));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 "));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f檢舉者: &6" + p.getName()));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f被檢舉者: &6" + target.getName()));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&f檢舉者所在伺服器:&6 " + p.getServer().getInfo().getName()));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f檢舉原因:"));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 "));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + var7));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7 "));
					mod.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-------------------------"));
				});
				this.report.add(p);
				ProxyServer.getInstance().getScheduler().schedule(Bungee.ins, () -> report.remove(p), 30L, TimeUnit.SECONDS);
			} else {
				p.sendMessage(
						ChatColor.translateAlternateColorCodes('&', "&8[&6檢舉系統&8]&7 使用方法: &c/report <玩家名稱> <原因>"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&4&l可檢舉的事件:&7 組隊, 吵架, 外掛(KillAura, Reach, HitBox, Fly, AutoClicker...)"));
			}
		}

	}

	@Override
	public HashSet<String> onTabComplete(CommandSender sender, String[] args) {
		final HashSet<String> matches = new HashSet<>();
		if (args.length == 2) {
			matches.add("組隊");
			matches.add("外掛");
		} else if (args.length == 1) {
			final Iterator<?> var5 = ProxyServer.getInstance().getPlayers().iterator();

			while (var5.hasNext()) {
				final ProxiedPlayer p = (ProxiedPlayer) var5.next();
				if (p.getName().startsWith(args[0])) {
					matches.add(p.getName());
				}
			}
		}

		return matches;
	}
}
