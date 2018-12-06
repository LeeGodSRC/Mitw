package net.development.mitw.utils.ttl;

public interface TtlHandler<E> {

	void onExpire(E element);

	long getTimestamp(E element);

}