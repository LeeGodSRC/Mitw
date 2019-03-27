package net.development.mitw.jedis;

import com.google.gson.JsonObject;

public interface JedisSubscriptionHandler {

	void handleMessage(JsonObject json);

}
