package net.development.mitw.chat.check;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public abstract class Check {
	private static ArrayList<Check> checks = new ArrayList<>();
	@Getter
	protected final List<String> checkExams = new ArrayList<>();
	protected String name;

	public Check(String name, List<String> exam) {
		for (final String s : exam)
			checkExams.add(s.toLowerCase());
		checks.add(this);
		this.name = name;
	}

	public abstract boolean isLegit(String str);

	public static Check isSafeMessage(String str) {
		str = str.toLowerCase();
		for (final Check c : checks)
			if (!c.isLegit(str))
				return c;
		return null;
	}

	public static Check getCheck(String name) {
		for (final Check c : checks) {
			if (c.getName().equalsIgnoreCase(name))
				return c;
		}
		return null;
	}

	public String getName() {
		return name;
	}

}
