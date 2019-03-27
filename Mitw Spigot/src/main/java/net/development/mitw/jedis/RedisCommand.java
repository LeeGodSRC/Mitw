package net.development.mitw.jedis;

import redis.clients.jedis.Jedis;

public interface RedisCommand<T> {

	T execute(Jedis redis);

}
