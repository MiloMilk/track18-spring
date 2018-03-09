package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List implements Stack, Queue  {

    Node current = null;
    int size = 0;

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    @Override
    void add(int item) {
        size++;
        if (current==null){
            current = new Node(null, null, item);
        } else {
            Node th = new Node(null, current, item);
            current.prev = th;
            current = th;
        }
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if ((idx<0)|(idx>size)){
            throw new NoSuchElementException();
        }
        Node for_return = current;
        size--;

        for (int i = size; i != idx; i--) {
            for_return = for_return.next;
        }

        Node t;
        t = for_return.prev;

        if (for_return.next != null) {
            for_return.next.prev = t;
        }
        if (for_return.prev != null) {
            for_return.prev.next = for_return.next;
        }

        return for_return.val;

    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if ((idx>=0)&(idx<size)) {
            Node for_return = current;
            for (int i = size-1; i != idx; i--) {
                for_return = for_return.next;
            }
            return for_return.val;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    int size() {
        return size;
    }

    @Override
    public void enqueue(int value) {
        add(value);
    }

    @Override
    public int dequeu() {
        return remove(0);
    }

    @Override
    public void push(int value) {
        add(value);
    }

    @Override
    public int pop() {
        return remove(size - 1);
    }

}
