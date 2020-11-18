package ImageGeneration;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class UniqueDeque<T> implements Deque<T> {

    Set<T> items = new HashSet<>();
    Deque<T> deque = new LinkedList<>();

    @Override
    public void clear() {
        items.clear();
        deque.clear();
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return deque.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return deque.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return deque.retainAll(collection);
    }

    @Override
    public Object[] toArray() {
        return deque.toArray();
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return deque.toArray(array);
    }

    @Override
    public boolean add(T item) {
        boolean itemAdded = false;
        if (items.contains(item) == false) {
            items.add(item);
            deque.add(item);
            itemAdded = true;
        }
        return itemAdded;
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean dequeChanged = false;
        for (T item : collection) {
            if (this.add(item) == true) {
                dequeChanged = true;
            }
        }
        return dequeChanged;
    }

    @Override
    public void addFirst(T item) {
        if (items.contains(item) == false) {
            items.add(item);
            deque.addFirst(item);
        }
    }

    @Override
    public void addLast(T item) {
        if (items.contains(item) == false) {
            items.add(item);
            deque.addLast(item);
        }
    }

    @Override
    public boolean contains(Object arg0) {
        return items.contains(arg0);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return deque.descendingIterator();
    }

    @Override
    public T element() {
        return deque.element();
    }

    @Override
    public T getFirst() {
        return deque.getFirst();
    }

    @Override
    public T getLast() {
        return deque.getLast();
    }

    @Override
    public Iterator<T> iterator() {
        return deque.iterator();
    }

    @Override
    public boolean offer(T item) {
        boolean addedToDedue = false;
        if (items.contains(item) == false) {
            addedToDedue = deque.offer(item);
        }
        if (addedToDedue) {
            items.add(item);
        }
        return addedToDedue;
    }

    @Override
    public boolean offerFirst(T item) {
        boolean addedToDedue = false;
        if (items.contains(item) == false) {
            addedToDedue = deque.offerFirst(item);
        }
        if (addedToDedue) {
            items.add(item);
        }
        return addedToDedue;
    }

    @Override
    public boolean offerLast(T item) {
        boolean addedToDedue = false;
        if (items.contains(item) == false) {
            addedToDedue = deque.offerLast(item);
        }
        if (addedToDedue) {
            items.add(item);
        }
        return addedToDedue;
    }

    @Override
    public T peek() {
        return deque.peek();
    }

    @Override
    public T peekFirst() {
        return deque.peekFirst();
    }

    @Override
    public T peekLast() {
        return deque.peekLast();
    }

    @Override
    public T poll() {
        T item = deque.poll();
        items.remove(item);
        return item;
    }

    @Override
    public T pollFirst() {
        T item = deque.pollFirst();
        items.remove(item);
        return item;
    }

    @Override
    public T pollLast() {
        T item = deque.pollLast();
        items.remove(item);
        return item;
    }

    @Override
    public T pop() {
        T item = deque.pop();
        items.remove(item);
        return item;
    }

    @Override
    public void push(T item) {
        if (items.contains(item) == false) {
            deque.push(item);
        }
    }

    @Override
    public T remove() {
        T item = deque.remove();
        items.remove(item);
        return item;
    }

    @Override
    public boolean remove(Object item) {
        boolean removedItem = false;
        if (items.contains(item)) {
            removedItem = deque.remove(item);
        }
        return removedItem;
    }

    @Override
    public T removeFirst() {
        T item = deque.removeFirst();
        items.remove(item);
        return item;
    }

    @Override
    public boolean removeFirstOccurrence(Object item) {
        // Since items in this deque are unique then there should only be one of any item
        return this.remove(item);
    }

    @Override
    public T removeLast() {
        T item = deque.removeLast();
        items.remove(item);
        return item;
    }

    @Override
    public boolean removeLastOccurrence(Object item) {
        // Since items in this deque are unique then there should only be one of any item
        return this.remove(item);
    }

    @Override
    public int size() {
        return deque.size();
    }
}
