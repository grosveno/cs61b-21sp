package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T> {
    private class Node {
        private T item;
        private Node prev;
        private Node next;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private int size;
    private Node sentinel;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    public void addFirst(T item) {
        Node head = sentinel.next;
        Node newNode = new Node(item, sentinel, head);
        sentinel.next = newNode;
        head.prev = newNode;

        size += 1;
    }

    public void addLast(T item) {
        Node tail = sentinel.prev;
        Node newNode = new Node(item, tail, sentinel);
        sentinel.prev = newNode;
        tail.next = newNode;

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

        Node first = sentinel.next;
        Node second = sentinel.next.next;

        T val = first.item;
        sentinel.next = second;
        second.prev = sentinel;

        size -= 1;
        return val;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        Node first = sentinel.prev;
        Node second = sentinel.prev.prev;

        T val = first.item;
        sentinel.prev = second;
        second.next = sentinel;

        size -= 1;
        return val;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        Node curr = sentinel.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.item;
    }

    private T getRecursive(Node curr, int index) {
        if (index == 0) {
            return curr.item;
        }

        return getRecursive(curr.next, index - 1);
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }

        return getRecursive(sentinel.next, index);
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        int wisPos;
        Node curr;

        public LinkedListDequeIterator() {
            wisPos = 0;
            curr = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return wisPos < size;
        }

        @Override
        public T next() {
            T val = curr.item;
            wisPos += 1;
            curr = curr.next;
            return val;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        // 事实上，由于类型擦除，我们无法确认o内部类型是否为T
        // 这里只是声明一下，让编译器能够通过
        LinkedListDeque<T> other = (LinkedListDeque<T>) o;
        if (this.size() != other.size()) {
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
