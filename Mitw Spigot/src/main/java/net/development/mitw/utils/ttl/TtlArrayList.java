package net.development.mitw.utils.ttl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

public class TtlArrayList<E> implements List<E>, TtlHandler<E> {
	private final List<E> store = new ArrayList<>();
	private final HashMap<E, Long> timestamps = new HashMap<>();
	private final long ttl;

	public TtlArrayList(final TimeUnit ttlUnit, final long ttlValue) {
		this.ttl = ttlUnit.toNanos(ttlValue);
	}

	private boolean expired(final E value) {
		return System.nanoTime() - this.timestamps.get(value) > this.ttl;
	}

	@Override
	public E get(final int index) {
		final E e = this.store.get(index);
		if (e != null && this.store.contains(e) && this.timestamps.containsKey(e) && this.expired(e)) {
			this.store.remove(e);
			this.timestamps.remove(e);
			return null;
		} else
			return e;
	}

	@Override
	public int indexOf(final Object o) {
		return this.store.indexOf(o);
	}

	@Override
	public int lastIndexOf(final Object o) {
		return this.store.lastIndexOf(o);
	}

	@Override
	public E set(final int index, final E e) {
		this.timestamps.put(e, System.nanoTime());
		return this.store.set(index, e);
	}

	@Override
	public boolean add(final E value) {
		this.timestamps.put(value, System.nanoTime());
		return this.store.add(value);
	}

	@Override
	public void add(final int i, final E value) {
		this.timestamps.put(value, System.nanoTime());
		this.store.add(i, value);
	}

	@Override
	public boolean addAll(final Collection<? extends E> c) {
		return this.store.addAll(c);
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends E> c) {
		return this.store.addAll(index, c);
	}

	@Override
	public int size() {
		return this.store.size();
	}

	@Override
	public boolean isEmpty() {
		return this.store.isEmpty();
	}

	@Override
	public boolean contains(final Object value) {
		if (value != null && this.store.contains(value) && this.timestamps.containsKey(value) && this.expired((E) value)) {
			this.store.remove(value);
			this.timestamps.remove(value);
			return false;
		} else
			return this.store.contains(value);
	}

	@Override
	public boolean remove(final Object value) {
		this.timestamps.remove(value);
		return this.store.remove(value);
	}

	@Override
	public E remove(final int i) {
		return this.store.remove(i);
	}

	@Override
	public boolean removeAll(final Collection<?> a) {
		final Iterator var2 = a.iterator();

		while(var2.hasNext()) {
			final Object object = var2.next();
			this.timestamps.remove(object);
		}

		return this.store.removeAll(a);
	}

	@Override
	public void clear() {
		this.timestamps.clear();
		this.store.clear();
	}

	@Override
	public Object[] toArray() {
		return this.store.toArray();
	}

	@Override
	public Object[] toArray(final Object[] a) {
		return this.store.toArray(a);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.store.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(final int a) {
		return this.store.listIterator(a);
	}

	@Override
	public Iterator<E> iterator() {
		return this.store.iterator();
	}

	@Override
	public boolean retainAll(final Collection<?> c) {
		return this.store.retainAll(c);
	}

	@Override
	public List<E> subList(final int fromIndex, final int toIndex) {
		return this.store.subList(fromIndex, toIndex);
	}

	@Override
	public boolean containsAll(final Collection<?> c) {
		return this.store.containsAll(c);
	}

	@Override
	public void onExpire(final E element) {

	}

	@Override
	public long getTimestamp(final E element) {
		return timestamps.get(element);
	}
}
