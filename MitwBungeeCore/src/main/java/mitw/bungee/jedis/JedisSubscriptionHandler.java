package mitw.bungee.jedis;

import com.google.gson.JsonObject;

public interface JedisSubscriptionHandler {

	void handleMessage(JsonObject json);

}
