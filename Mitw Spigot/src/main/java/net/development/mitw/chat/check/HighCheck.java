package net.development.mitw.chat.check;

import java.util.List;

public class HighCheck extends Check {
	public HighCheck(List<String> list) {
		super(list);
	}

	@Override
	public boolean isLegit(String str) {
		for (final String check : checkExams) {
			if (str.contains(check)) {
				return false;
			}
		}
		return true;
	}

}
