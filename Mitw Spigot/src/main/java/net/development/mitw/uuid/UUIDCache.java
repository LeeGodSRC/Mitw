package net.development.mitw.uuid;

import net.development.mitw.Mitw;
import net.development.mitw.utils.FastUUID;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UUIDCache {

	private static Map<String, UUID> nameToUuid = new HashMap<>();
	private static Map<UUID, String> uuidToName = new HashMap<>();

	public static String getName(final UUID uuid) {
		if (uuidToName.containsKey(uuid))
			return uuidToName.get(uuid);

		return "Unknown";
	}

	public static UUID getUuid(final String name) {
		if (nameToUuid.containsKey(name.toLowerCase()))
			return nameToUuid.get(name.toLowerCase());
		return null;
	}

	public static void feteh() {
		Mitw.getInstance().getMitwJedis().runCommand(redis -> {
			Map<String, String> cached = redis.hgetAll("name-to-uuid");

			if (cached == null || cached.isEmpty()) {
				return null;
			}

			Map<String, UUID> ntu = new HashMap<>();
			Map<UUID, String> utn = new HashMap<>();

			for (Map.Entry<String, String> entry : cached.entrySet()) {
				ntu.put(entry.getKey(), FastUUID.parseUUID(entry.getValue()));
				utn.put(FastUUID.parseUUID(entry.getValue()), entry.getKey());
			}

			nameToUuid = ntu;
			uuidToName = utn;

			return null;
		});
	}

	public static void update(final String name, final UUID uuid) {
		nameToUuid.put(name.toLowerCase(), uuid);
		uuidToName.put(uuid, name);
		Mitw.getInstance().getMitwJedis().runCommand(redis -> {
			redis.hset("name-to-uuid", name.toLowerCase(), FastUUID.toString(uuid));
			redis.hset("uuid-to-name", FastUUID.toString(uuid), name);
			return null;
		});
	}

}
