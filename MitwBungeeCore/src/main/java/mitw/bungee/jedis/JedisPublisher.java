package mitw.bungee.jedis;

import mitw.bungee.Mitw;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

@RequiredArgsConstructor
public class JedisPublisher {

	private final JedisSettings jedisSettings;

	public void write(String channel, JsonObject payload) {
		Validate.notNull(Mitw.INSTANCE.getMitwJedis().getPool());

		Mitw.INSTANCE.getMitwJedis().runCommand(redis -> {
			if (jedisSettings.hasPassword()) {
				redis.auth(jedisSettings.getPassword());
			}

			redis.publish(channel, payload.toString());

			return null;
		});
	}

}
