package net.development.mitw.chat.check;

import java.util.ArrayList;
import java.util.List;

public abstract class Check {
	private static ArrayList<Check> checks = new ArrayList<>();

	protected final List<String> checkExams = new ArrayList<>();

	public Check(List<String> exam) {
		for (final String s : exam)
			checkExams.add(s.toLowerCase());
		checks.add(this);
	}

	public abstract boolean isLegit(String str);

	public static boolean isSafeMessage(String str) {
		str = str.toLowerCase();
		for (final Check c : checks)
			if (!c.isLegit(str))
				return false;
		return true;
	}

}
