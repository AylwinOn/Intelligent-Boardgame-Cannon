package com.cannon.pgn;

public class HashMap<K, V> {
    private Entry<K, V>[] entries;
    private int capacity;
    private int size = 0;


    @SuppressWarnings("unchecked")
    public HashMap() {
        this.capacity = 32;
        this.entries = new Entry[this.capacity];
    }

    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
        this.capacity = capacity;
        this.entries = new Entry[this.capacity];
    }


    public void put(K key, V value) {
        if (size >= 0.75 * this.capacity) {
            this.extend();
        }

        Entry<K, V> entry = new Entry<>(key, value, null);
        int index = Math.abs(getHash(key) % this.capacity);

        Entry<K, V> current = this.entries[index];
        if (current == null) {
            this.entries[index] = entry;
            this.size++;
        } else {
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
            } else {
                current.next = entry;
                this.size++;
            }
        }
    }


    public V get(K key) {
        Entry<K, V> entry = this.entries[Math.abs(getHash(key) % this.capacity)];
        while (entry != null) {
            if (key == entry.key) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }


    public int size() {
        return this.size;
    }


    public boolean isEmpty() {
        return size == 0;
    }


    private int getHash(K key) {
        return key.hashCode();
    }

    /**
     * Extends the length of the inner array by two to keep the efficiency of
     * the HashMap.
     */
    @SuppressWarnings("unchecked")
    private void extend() {
        this.size = 0;
        this.capacity *= 2;
        Entry<K, V>[] oldEntries = this.entries;
        this.entries = new Entry[this.capacity];
        for (Entry<K, V> entry : oldEntries) {
            while (entry != null) {
                this.put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    /**
     * A class that stores key and value pairs. This is used to store entries
     * to the HashMap.
     */
    static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        /**
         * Constructor that creates the instance of the Entry class.
         *
         * @param key the key of the to-be-stored element.
         * @param value the element itself that is stored.
         * @param next the next value in the linked list.
         */
        public Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
