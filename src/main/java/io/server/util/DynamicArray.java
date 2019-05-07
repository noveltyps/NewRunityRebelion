package io.server.util;

import java.lang.reflect.Array;


@SuppressWarnings("unchecked")
public class DynamicArray<E> {

    private int count;

    private E[] values;

    private final int expandAmount;

    public DynamicArray(int initialCapacity) {
        this.values = (E[]) new Object[initialCapacity];
        this.expandAmount = Math.max(1, (int) (initialCapacity * 0.10));
    }

    /**
     * Add
     */

    public int add(E value) {
        ensureCapacity();
        int freeIndex = indexOf(null);
        if(freeIndex == -1) {
            System.err.println("DynamicArray - Could not add value! | " + new Throwable().getStackTrace()[1].toString());
            return -1;
        }
        values[freeIndex] = value;
        count++;
        return freeIndex;
    }

    /**
     * Remove value
     */

    public int remove(E value) {
        int index = indexOf(value);
        if(index != -1)
            fastRemove(index);
        return index;
    }

    /**
     * Remove index
     */

    public void remove(int index) {
        if(values[index] == null) {
            System.err.println("DynamicArray - Could not remove value! | " + new Throwable().getStackTrace()[1].toString());
            return;
        }
        fastRemove(index);
    }

    /**
     * Fast remove index
     */

    public void fastRemove(int index) {
        values[index] = null;
        count--;
    }

    /**
     * Clear
     */

    public void clear() {
        int length = values.length;
        for(int index = 0; index < length; index++)
            values[index] = null;
        count = 0;
    }

    /**
     * Index of value
     */

    public int indexOf(E value) {
        if(value == null) {
            int length = values.length;
            for(int index = 0; index < length; index++) {
                if(values[index] == null)
                    return index;
            }
        } else {
            int length = values.length;
            for(int index = 0; index < length; index++) {
                if(value.equals(values[index]))
                    return index;
            }
        }
        return -1;
    }

    /**
     * To array
     */

    public E[] toArray(Class<?> type) {
        int arrayIndex = 0;
        E[] array = (E[]) Array.newInstance(type, count);
        int highestIndex = values.length;
        for(int index = 0; index < highestIndex; index++) {
            E value = values[index];
            if(value != null)
                array[arrayIndex++] = value;
        }
        return array;
    }

    /**
     * Ensure capacity
     */

    private void ensureCapacity() {
        int length = values.length;
        if(count < length)
            return;
        E[] expanded = (E[]) new Object[length + expandAmount];
        System.arraycopy(values, 0, expanded, 0, length);
        values = expanded;
    }

    /**
     * Get index
     */

    public E get(int index) {
        return values[index];
    }

    /**
     * Contains
     */

    public boolean contains(E value) {
        return indexOf(value) >= 0;
    }

    /**
     * Count
     */

    public int count() {
        return count;
    }

    /**
     * Empty
     */

    public boolean isEmpty() {
        return count <= 0;
    }

    /**
     * Length
     */

    public int length() {
        return values.length;
    }

}
