package net.development.mitw.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Entry<K, V> {

    private final K key;
    private final V value;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Entry && ((Entry) obj).getKey().equals(key) && ((Entry) obj).getValue().equals(value);
    }

    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode();
    }
}
