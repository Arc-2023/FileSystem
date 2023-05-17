package org.shirakawatyu.filesystem.pojo;

public class Item<V> {
    public int index;
    public V v;

    public Item(int index, V v) {
        this.index = index;
        this.v = v;
    }
}
