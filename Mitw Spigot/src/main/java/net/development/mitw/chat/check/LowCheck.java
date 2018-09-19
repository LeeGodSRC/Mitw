package net.development.mitw.chat.check;

import java.util.List;

import net.development.mitw.config.Settings;

public class LowCheck extends Check {
	public LowCheck(List<String> list) {
		super(list);
	}

	@Override
	public boolean isLegit(String str) {
		final String[] strs = str.split(" ");
		for (final String s : strs)
			for (final String check : checkExams) {
				if (s.contains(check) && !isException(s))
					return false;
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
