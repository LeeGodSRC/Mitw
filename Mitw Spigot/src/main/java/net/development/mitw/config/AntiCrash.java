package net.development.mitw.config;

import java.util.List;

public class AntiCrash extends SimpleConfig {

	public static List<String> ACTIONS;

	public AntiCrash(String fileName) {
		super(fileName);
		ACTIONS = getStringList("actions");
	}

	public static void init() {
		new AntiCrash("anticrash.yml");
	}

}
