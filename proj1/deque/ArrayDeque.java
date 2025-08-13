package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {
    private static final int UP_RADIX = 2;
    private static final int DOWN_RADIX = 4;
    private static final int INITIAL_FIRST_NEXT = 3;
    private static final int INITIAL_LAST_NEXT = 4;
    private static final int INITIAL_CAPACITY = 8;

    private int size;
    private int firstNext;
    private int lastNext;
    private T[] items;

    public ArrayDeque() {
        size = 0;
        firstNext = INITIAL_FIRST_NEXT;
        lastNext = INITIAL_LAST_NEXT;
        items = (T[]) new Object[INITIAL_CAPACITY];
    }

    private int getNextIndex(int index, int capacity) {
        return (index + 1) % capacity;
    }

    private int getPrevIndex(int index, int capacity) {
        return (index + capacity - 1) % capacity;
    }

    private void resize(int capacity) {
        // 将原数组的元素复制到新数组中
        int newFirstNext = INITIAL_FIRST_NEXT;
        T[] newItems = (T[]) new Object[capacity];

        int p = getNextIndex(firstNext, items.length);  // p point to items
        int q = getNextIndex(newFirstNext, capacity);   // q point to newItems
        for (int i = 0; i < size; i++) {
            T item = get(i);
            newItems[q] = item;
            items[p] = null;

            p = getNextIndex(p, items.length);
            q = getNextIndex(q, capacity);
        }

        firstNext = newFirstNext;
        lastNext = q;
        items = newItems;
    }

    public void addFirst(T item) {
        if (size == items.length) {
            resize(items.length * UP_RADIX);
        }

        items[firstNext] = item;
        firstNext = getPrevIndex(firstNext, items.length);

        size += 1;
    }

    public void addLast(T item) {
        if (size == items.length) {
            resize(items.length * UP_RADIX);
        }

        items[lastNext] = item;
        lastNext = getNextIndex(lastNext, items.length);

        size += 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        for (T item : this) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        // 当大小比容量小的多，且容量比初始容量大的情况下，缩小
        // 且最小缩小成初始容量
        if (size < items.length / 4 && items.length > INITIAL_CAPACITY) {
            int capacity = Math.max(items.length / DOWN_RADIX, INITIAL_CAPACITY);
            resize(capacity);
        }
        int first = getNextIndex(firstNext, items.length);

        T item = items[first];
        items[first] = null;

        firstNext = first;
        size -= 1;
        return item;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        if (size < items.length / 4 && items.length > INITIAL_CAPACITY) {
            int capacity = Math.max(items.length / DOWN_RADIX, INITIAL_CAPACITY);
            resize(capacity);
        }
        int last = getPrevIndex(lastNext, items.length);

        T item = items[last];
        items[last] = null;

        lastNext = last;
        size -= 1;
        return item;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        return items[(firstNext + 1 + index) % items.length];
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ArrayDeque)) {
            return false;
        }
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if (other.size() != this.size()) {
            return false;
        }
        Iterator<T> itOther = other.iterator();
        for (T val1 : this) {
            // 真正确认o内部类型是否为T在这里的equals方法
            T val2 = itOther.next();
            if (!val1.equals(val2)) {
                return false;
            }
        }
        return true;
    }
}
