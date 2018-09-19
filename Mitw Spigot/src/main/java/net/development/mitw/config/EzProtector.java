package net.development.mitw.config;

import lombok.Getter;

public class EzProtector extends SimpleConfig {

	@Getter
	public static EzProtector instance;

	public EzProtector(String fileName) {
		super(fileName);
		instance = this;
	}

	public static void init() {
		new EzProtector("protector.yml");
	}

}
