package fr.twitmund.modSuite.utils;

public class Pair<K, V> {

    private final K Key;
    private final V Value;

    public Pair(K key, V value) {
        this.Key = key;
        this.Value = value;
    }
    public Pair() {
        this.Key = null;
        this.Value = null;
    }

    /**
     * Create a new pair
     * @param key The left value
     * @param value The right value
     * @param <K> The type of the left value
     * @param <V> The type of the right value
     * @return The new pair
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    /**
     * Get the left value
     * @return The left value
     */
    public K getKey() {
        return Key;
    }

    /**
     * Get the right value
     * @return The right value
     */
    public V getValue() {
        return Value;
    }

}
