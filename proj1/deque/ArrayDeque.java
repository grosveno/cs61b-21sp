package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {
    /* Constant values */
    private static final int UP_RADIX = 2;
    private static final int DOWN_RADIX = 4;
    private static final int INITIAL_SIZE = 8;
    private static final int INITIAL_FIRST_PREV = 3;
    private static final int INITIAL_LAST_NEXT = 4;


    /* ArrayDequeIterator class */
    private class ArrayDequeIterator implements Iterator<T> {
        int wisPos;

        public ArrayDequeIterator() {
            wisPos = 0;
        }

        @Override
        public boolean hasNext() {
            return wisPos < size;
        }

        @Override
        public T next() {
            T val = get(wisPos);
            wisPos += 1;
            return val;
        }
    }


    /* ArrayDeque class's members */
    private int size;
    private int firstPrev;
    private int lastNext;
    private T[] items;


    /* Function: construct */
    public ArrayDeque() {
        size = 0;
        firstPrev = INITIAL_FIRST_PREV;
        lastNext = INITIAL_LAST_NEXT;
        items = (T[]) new Object[INITIAL_SIZE];
    }

    /* Function: addFirst */
    public void addFirst(T item) {
        bigger();
        items[firstPrev] = item;
        firstPrev = getPrevIndex(firstPrev, items.length);
        size += 1;
    }

    /* Function: addLast */
    public void addLast(T item) {
        bigger();
        items[lastNext] = item;
        lastNext = getNextIndex(lastNext, items.length);
        size += 1;
    }

    /* Function: isEmpty */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Function: size */
    public int size() {
        return size;
    }

    /* Function: printDeque
    * Behave: use iterator */
    public void printDeque() {
        for (T val : this) {
            System.out.print(val + " ");
        }
        System.out.println();
    }

    /* Function: removeFirst */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        smaller();
        int first = getNextIndex(firstPrev, items.length);
        T val = items[first];
        items[first] = null;
        firstPrev = first;
        size -= 1;
        return val;
    }

    /* Function: removeLast */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        smaller();
        int last = getPrevIndex(lastNext, items.length);
        T val = items[last];
        items[last] = null;
        lastNext = last;
        size -= 1;
        return val;
    }

    /* Function: get */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return items[(firstPrev + 1 + index) % items.length];
    }

    /* Function: iterator */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    /* Function: equals
    * Behave: use iterator */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if (this.size() != other.size()) {
            return false;
        }
        Iterator<T> p = other.iterator();
        for (T val1 : this) {
            T val2 = p.next();
            if (!val1.equals(val2)) {
                return false;
            }
        }
        return true;
    }

    /* Helper Function: getPrevIndex */
    private int getPrevIndex(int index, int length) {
        return (index + length - 1) % length;
    }

    /* Helper Function: getNextIndex */
    private int getNextIndex(int index, int length) {
        return (index + 1) % length;
    }

    /* Helper Function: resize */
    private void resize(int capacity) {
        T[] newItems = (T[]) new Object[capacity];
        int p = getNextIndex(firstPrev, items.length);
        int q = getNextIndex(INITIAL_FIRST_PREV, capacity);
        for (int i = 0; i < size; i++) {
            T val = items[p];
            newItems[q] = val;
            p = getNextIndex(p, items.length);
            q = getNextIndex(q, capacity);
        }
        firstPrev = INITIAL_FIRST_PREV;
        lastNext = q;
        items = newItems;
    }

    /* Helper Function: bigger */
    private void bigger() {
        if (size == items.length) {
            resize(UP_RADIX * size);
        }
    }

    /* Helper Function: smaller */
    private void smaller() {
        if (size < items.length / 4 && size > INITIAL_SIZE) {
            int capacity = Math.max(items.length / DOWN_RADIX, INITIAL_SIZE);
            resize(capacity);
        }
    }
}
