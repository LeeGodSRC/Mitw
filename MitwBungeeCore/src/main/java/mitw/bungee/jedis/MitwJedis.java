package mitw.bungee.jedis;

import mitw.bungee.Mitw;
import mitw.bungee.jedis.impl.MitwSubscriptionHandler;
import com.google.gson.JsonObject;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Getter
public class MitwJedis {

	private JedisSettings settings;
	private JedisPool pool;
	private JedisPublisher publisher;
	private JedisSubscriber subscriber;

	public MitwJedis(JedisSettings settings) {
		this.settings = settings;

		ClassLoader previous = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(MitwJedis.class.getClassLoader());
		this.pool = new JedisPool(this.settings.getAddress(), this.settings.getPort());
		Thread.currentThread().setContextClassLoader(previous);

		try (Jedis jedis = this.pool.getResource()) {
			if (this.settings.hasPassword()) {
				jedis.auth(this.settings.getPassword());
			}

			this.publisher = new JedisPublisher(this.settings);
			this.subscriber = new JedisSubscriber("mitw", this.settings, new MitwSubscriptionHandler());
		}
	}

	public static MitwJedis getInstance() {
		return Mitw.INSTANCE.getMitwJedis();
	}

	public boolean isActive() {
		return this.pool != null && !this.pool.isClosed();
	}

	public void write(String payload, JsonObject data) {
		JsonObject object = new JsonObject();

		object.addProperty("payload", payload);
		object.add("data", data == null ? new JsonObject() : data);

		this.publisher.write("mitw", object);
	}

	public <T> T runCommand(RedisCommand<T> redisCommand) {
		Jedis jedis = this.pool.getResource();
		T result = null;

		try {
			result = redisCommand.execute(jedis);
		} catch (Exception e) {
			e.printStackTrace();

			if (jedis != null) {
				this.pool.returnBrokenResource(jedis);
				jedis = null;
			}
		} finally {
			if (jedis != null) {
				this.pool.returnResource(jedis);
			}
		}

		return result;
	}

}
