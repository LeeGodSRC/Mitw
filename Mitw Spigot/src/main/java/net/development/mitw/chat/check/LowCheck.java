package net.development.mitw.chat.check;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.development.mitw.config.Settings;

public class LowCheck extends Check {
	public LowCheck(List<String> list) {
		super("low", list);
	}

	@Override
	public boolean isLegit(String str) {
		final String[] strs = str.split(" ");
		for (final String s : strs) {
			for (final Player p : Bukkit.getOnlinePlayers()) {
				if (p.getName().toLowerCase().contains(s))
					return true;
			}
			for (final String check : checkExams) {
				if (s.contains(check) && !isException(s))
					return false;
			}
		}
		return true;
	}

	public boolean isException(String str) {
		for (final String ex : Settings.CHECK_EXCEPTION) {
			if (str.contains(ex)) {
				return true;
			}
		}
		return false;
	}

}
