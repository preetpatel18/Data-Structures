package venom;
/*
 * Queue implementation using a Circular Linked List
 * Enqueues at the end and dequeues from the front of the list.
 */
public class Queue<T> {
    
    /* 
     * Node class used ONLY inside the Queue class.
     */
    private class Node {
        private T    item;
        private Node next;
        
        public Node (T item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

    // reference to the last node in the linked list
    private Node last;  

    /*
     * Constructor initializes an empty queue
     */
    public Queue () {
        last = null;
    }

    /*
     * Returns true if the queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return last == null;
    }
    
    /*
     * Enqueue at the end of the list.
     * @param item is the data/item to enqueue.
     */
    public void enqueue (T item) {
        
        Node newNode = new Node(item, null); // create the new node
        
        if ( isEmpty() ) {
            // Currently empty: last points to itself
            last = newNode;
            last.next = last;
        } else {
            // Update new node's next to original front
            newNode.next = last.next;
            // Update old last's next to new node
            last.next = newNode;
            // Update last reference directly
            last = newNode;
        }
    }
    
    /*
     * Dequeue from the front of the CLL.
     * @return the dequeued item.
     */
    public T dequeue() {

        if ( isEmpty() ) { 
            return null;
        }
        
        // front of queue
        Node first = last.next;
        T item = first.item;  

        if ( last == last.next ) {
            // Only one node in the queue, empty the queue
            last = null;
        } else {
            // update `last.next` to the next node
            last.next = first.next;
        }

        return item;
    }

}
