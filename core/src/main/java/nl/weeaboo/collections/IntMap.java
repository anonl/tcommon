package nl.weeaboo.collections;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;

/**
 * Maps ints to Objects.
 */
public final class IntMap<V> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Object REMOVED = new Object();

    /** Sorted array of keys. */
    private int[] keys;

    /**
     * Associated values for each key index. Uses {@link #REMOVED} to indicate an invalid/removed slot.
     */
    private Object[] values;

    /**
     * Only the first {@code length} slots contain values.
     */
    private int length;

    /** The number of valid slots, ignoring removed slots. */
    private int size;

    /** Internal modification counter used to detect concurrent modification errors. */
    private int modCount;

    /**
     * Creates an empty int map with the default initial capacity.
     */
    public IntMap() {
        this(8);
    }

    /**
     * Creates an empty int map with the specified initial capacity.
     */
    public IntMap(int initialCapacity) {
        keys = new int[initialCapacity];
        values = new Object[initialCapacity];
    }

    /**
     * Creates an int map from an existing {@link Map}.
     */
    public IntMap(Map<Integer, ? extends V> map) {
        this(map.size());

        // Add keys
        int t = 0;
        for (Integer key : map.keySet()) {
            keys[t++] = key.intValue();
        }
        Arrays.sort(keys, 0, t);

        // Add values (must be done after sorting)
        for (int n = 0; n < t; n++) {
            values[n] = map.get(keys[n]);
        }
    }

    private void reserve(int capacity) {
        if (capacity < length) {
            throw new IllegalArgumentException(
                    "New capacity (" + capacity + ") is smaller than the current length (" + length + ")");
        }

        int[] newKeys = new int[capacity];
        Object[] newVals = new Object[capacity];

        System.arraycopy(keys, 0, newKeys, 0, length);
        System.arraycopy(values, 0, newVals, 0, length);

        keys = newKeys;
        values = newVals;
    }

    /**
     * Removes the values stored under {@code key}.
     *
     * @return The value previously associated with {@code key}, or {@code null} if no such value exists.
     */
    public V remove(int key) {
        int i = Arrays.binarySearch(keys, 0, length, key);
        if (i >= 0 && values[i] != REMOVED) {
            @SuppressWarnings("unchecked")
            final V oldval = (V)values[i];
            values[i] = REMOVED;
            size--;
            modCount++;
            return oldval;
        }
        return null;
    }

    private void compact() {
        if (size == length) {
            return; // Already compacted
        }

        int removed = 0;
        for (int n = 0; n < length; n++) {
            Object val = values[n];
            if (val == REMOVED) {
                removed++;
            } else if (removed > 0) {
                keys[n - removed] = keys[n];
                values[n - removed] = val;
            }
        }
        for (int n = length - removed; n < length; n++) {
            values[n] = null;
        }
        length -= removed;
        modCount++;
    }

    /**
     * Completely clears the map, removing all entries.
     */
    public void clear() {
        Arrays.fill(values, 0, length, null);
        length = 0;
        size = 0;
        modCount++;
    }

    /**
     * Returns an iterable for iterating over the values in this map.
     */
    public Iterable<V> values() {
        return new Iterable<V>() {
            @Override
            public Iterator<V> iterator() {
                return new Iterator<V>() {

                    private final int expectedModCount = modCount;
                    private int cursor = -1;

                    private void checkModCount() {
                        if (modCount != expectedModCount) {
                            throw new ConcurrentModificationException();
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        return cursor + 1 < size;
                    }

                    @Override
                    public V next() {
                        checkModCount();
                        return valueAt(++cursor);
                    }

                    @Override
                    public void remove() {
                        checkModCount();
                        removeAt(cursor);
                    }
                };
            }
        };
    }

    /**
     * Returns a copy of the keys used in this map.
     */
    public int[] getKeys() {
        compact();

        int[] result = new int[length];
        System.arraycopy(keys, 0, result, 0, length);
        return result;
    }

    /**
     * Checks if the specified key exists in this map.
     */
    public boolean containsKey(int key) {
        int i = Arrays.binarySearch(keys, 0, length, key);
        return i >= 0 && values[i] != REMOVED;
    }

    /**
     * Returns the value associated with the given key, or {@code null} if no such value exists.
     */
    public V get(int key) {
        int i = Arrays.binarySearch(keys, 0, length, key);
        if (i < 0 || values[i] == REMOVED) {
            return null;
        }

        @SuppressWarnings("unchecked")
        V val = (V) values[i];
        return val;
    }

    /**
     * Returns {@code true} if this map has zero entries.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of entries in this map.
     */
    public int size() {
        return size;
    }

    private void checkIndex(int index) {
        compact(); // Clear removed slots

        if (index >= length) {
            throw new ArrayIndexOutOfBoundsException("Index (" + index + ") out of bounds, size=" + size);
        }
    }

    /**
     * Returns the key stored at the specified internal index.
     */
    public int keyAt(int index) {
        compact();
        checkIndex(index);

        return keys[index];
    }

    /**
     * Returns the value stored at the specified internal index.
     */
    public V valueAt(int index) {
        compact();
        checkIndex(index);

        @SuppressWarnings("unchecked")
        V val = (V)values[index];
        return val;
    }

    /**
     * Adds all entries in the supplied map to this map.
     */
    public <T extends V> void putAll(Map<Integer, T> map) {
        putAll(new IntMap<T>(map));
    }

    /**
     * Adds all entries in the supplied map to this map.
     *
     * @throws IllegalArgumentException When trying to add a map to itself.
     */
    public void putAll(IntMap<? extends V> map) {
        if (this == map) {
            throw new IllegalArgumentException("Cannot add map to itself");
        }

        for (int n = 0; n < map.length; n++) {
            if (map.values[n] != REMOVED) {
                @SuppressWarnings("unchecked")
                V val = (V)map.values[n];
                put(map.keys[n], val);
            }
        }
    }

    /**
     * Sets the value for a certain key.
     *
     * @return The value previously associated with the key, or {@code null} if no such value exists.
     */
    public V put(int key, V val) {
        modCount++;

        int i = Arrays.binarySearch(keys, 0, length, key);
        if (i >= 0) {
            // Key already exists, overwrite exiting value
            return doPutAtIndex(i, val);
        }

        // Find insertion index
        i = -(i + 1);

        if (i < length && values[i] == REMOVED) {
            // Key doesn't exist, but the value following it has been deleted so we can use its slot
            keys[i] = key;
            values[i] = val;
            size++;
            return null; //Old value was a dummy
        }

        if (size < length && length >= keys.length) {
            // Array is full, but contains gaps so try to compact in order to make room
            compact();

            // Update i for the new situation
            i = Arrays.binarySearch(keys, 0, length, key);
            i = -(i + 1);
        }

        if (length >= keys.length) {
            // We need more room for the new index
            reserve(length + 16);
        }

        if (i < length) {
            // Move the existing entries (following out insertion point) one
            // position further down the list to make room
            System.arraycopy(keys, i, keys, i + 1, length - i);
            System.arraycopy(values, i, values, i + 1, length - i);
        }

        keys[i] = key;
        values[i] = val;
        size++;
        length++;
        return null; // Old value doesn't exist
    }

    /**
     * Removed the value stored at the specified internal index.
     *
     * @return The value previously stored at that index, or {@code null} if no such value exists.
     */
    public V removeAt(int index) {
        compact();
        checkIndex(index);

        modCount++;

        Object oldval = values[index];
        if (oldval == REMOVED) {
            return null;
        }

        values[index] = REMOVED;
        size--;

        @SuppressWarnings("unchecked")
        V result = (V)oldval;
        return result;
    }

    /**
     * Overwrites the value stored at the specified internal index.
     *
     * @return The value previously stored at that index, or {@code null} if no such value exists.
     */
    public V putAtIndex(int index, V val) {
        compact();
        checkIndex(index);

        modCount++;

        return doPutAtIndex(index, val);
    }

    private V doPutAtIndex(int index, V val) {
        Object oldval = values[index];
        if (oldval == REMOVED) {
            oldval = null;
            size++;
        }
        values[index] = val;

        @SuppressWarnings("unchecked")
        V result = (V)oldval;
        return result;
    }

}
