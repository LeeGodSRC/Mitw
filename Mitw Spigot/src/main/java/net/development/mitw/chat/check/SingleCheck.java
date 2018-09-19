package net.development.mitw.chat.check;

import java.util.List;

public class SingleCheck extends Check {
	public SingleCheck(List<String> list) {
		super(list);
	}

	@Override
	public boolean isLegit(String str) {
		final String[] strs = str.split(" ");
		for (final String s : strs) {
			for (final String exam : checkExams) {
				if (s.equalsIgnoreCase(exam)) {
					return false;
				}
			}
		}
		return true;
	}

}
