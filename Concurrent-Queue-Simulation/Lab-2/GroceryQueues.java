import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GroceryQueues {
    private final int cashiers;
    private final int maxQueueSize;
    private final List<Queue<Customer>> queues;
    private final Lock lock = new ReentrantLock();

    public GroceryQueues(int cashiers, int maxQueueSize) {
        this.cashiers = cashiers;
        this.maxQueueSize = maxQueueSize;
        this.queues = new ArrayList<>(cashiers);
        for (int i = 0; i < cashiers; i++) {
            queues.add(new LinkedList<>());
        }
    }

    public boolean addCustomer(Customer customer, int currentTime) {
        lock.lock();
        try {
            Queue<Customer> bestQueue = null;
            for (Queue<Customer> queue : queues) {
                if (queue.size() < maxQueueSize) {
                    if (bestQueue == null || queue.size() < bestQueue.size()) {
                        bestQueue = queue;
                    }
                }
            }

            if (bestQueue == null) {
                return false; // All queues are full
            }

            bestQueue.offer(customer);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void serveCustomers(int currentTime) {
        lock.lock();
        try {
            for (Queue<Customer> queue : queues) {
                if (!queue.isEmpty()) {
                    Customer customer = queue.poll();
                    customer.setServed(true);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int getTotalQueueSize() {
        lock.lock();
        try {
            return queues.stream().mapToInt(Queue::size).sum();
        } finally {
            lock.unlock();
        }
    }
}
