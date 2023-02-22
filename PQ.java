package cs2030.util;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PQ<T> {
    private final Comparator<? super T> cmp;
    private final PriorityQueue<? extends T> pq;
    private static final int INNIT = 5000;

    public PQ(Comparator<? super T> cmp) {
        this.cmp = cmp;
        this.pq = new PriorityQueue<T>(INNIT, cmp);
    }

    private PQ(Comparator<? super T> cmp, PriorityQueue<? extends T> queue) {
        this.cmp = cmp;
        this.pq = queue;
    }

    public PQ(Comparator<? super T> cmp, PQ<? extends T> oldpq) {
        this.cmp = cmp;
        this.pq = oldpq.pq;
    }

    public PQ<T> add(T elem) {
        PriorityQueue<T> newQ = new PriorityQueue<T>(this.pq);
        newQ.add(elem);
        PQ<T> newPQ = new PQ<T>(cmp, newQ); 
        return newPQ;
    }


    public Pair<T, PQ<T>> poll() {
        PriorityQueue<T> newQ = new PriorityQueue<T>(this.pq);
        T elem = newQ.poll();
        PQ<T> newPQ = new PQ<T>(this.cmp, newQ);
        return Pair.of(elem, newPQ);
    }
    
    public boolean isEmpty() {
        return this.pq.isEmpty();
    }

    @Override
    public String toString() {
        return this.pq.toString();
    }
}


