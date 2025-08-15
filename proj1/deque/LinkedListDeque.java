package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T> {
    /* Node class */
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

    /* LinkedListDequeIterator class */
    private class LinkedListDequeIterator implements Iterator<T> {
        private int wisPos;
        private Node curr;

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


    /* LinkedListDeque class's members */
    private int size;
    private Node sentinel;


    /* Function: construct */
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /* Function: addFirst
    * Behave: insert newNode between sentinel and head */
    public void addFirst(T item) {
        Node head = sentinel.next;
        Node newNode = new Node(item, sentinel, head);
        head.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    /* Function: addLast
     * Behave: insert newNode between tail and sentinel */
    public void addLast(T item) {
        Node tail = sentinel.prev;
        Node newNode = new Node(item, tail, sentinel);
        sentinel.prev = newNode;
        tail.next = newNode;
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

    /* Function: removeFirst
    * Behave: if size == 0, do nothing;
    *         else remove the first node */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        Node first = sentinel.next;
        Node second = first.next;
        T val = first.item;
        sentinel.next = second;
        second.prev = sentinel;
        size -= 1;
        return val;
    }

    /* Function: removeLast
     * Behave: if size == 0, do nothing;
     *         else remove the last node */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        Node lastFirst = sentinel.prev;
        Node lastSecond = lastFirst.prev;
        T val = lastFirst.item;
        sentinel.prev = lastSecond;
        lastSecond.next = sentinel;
        size -= 1;
        return val;
    }

    /* Function: get
    * Behave: get the ith item */
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

    /* Function: getRecursive
     * Behave: get the ith item */
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getRecursive(sentinel.next, index);
    }

    /* Function: iterator */
    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    /* Function: equals
    * Behave: use iterator */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LinkedListDeque)) {
            return false;
        }
        LinkedListDeque<T> other = (LinkedListDeque<T>) o;
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

    /* Helper Function: getRecursive */
    private T getRecursive(Node curr, int index) {
        if (index == 0) {
            return curr.item;
        }
        return getRecursive(curr.next, index - 1);
    }
}
