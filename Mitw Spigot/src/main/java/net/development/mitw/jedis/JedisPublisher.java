package net.development.mitw.jedis;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import net.development.mitw.Mitw;
import org.apache.commons.lang3.Validate;

@RequiredArgsConstructor
public class JedisPublisher {

	private final JedisSettings jedisSettings;

	public void write(String channel, JsonObject payload) {
		Validate.notNull(Mitw.getInstance().getMitwJedis().getPool());

		Mitw.getInstance().getMitwJedis().runCommand(redis -> {
			if (jedisSettings.hasPassword()) {
				redis.auth(jedisSettings.getPassword());
			}

			redis.publish(channel, payload.toString());

			return null;
		});
	}

}
