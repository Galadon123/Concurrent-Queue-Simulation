import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankQueue {
    private final int tellers;
    private final int maxQueueSize;
    private final Queue<Customer> queue;
    private final Lock lock = new ReentrantLock();

    public BankQueue(int tellers, int maxQueueSize) {
        this.tellers = tellers;
        this.maxQueueSize = maxQueueSize;
        this.queue = new LinkedList<>();
    }

    public boolean addCustomer(Customer customer) {
        lock.lock();
        try {
            if (queue.size() >= maxQueueSize) {
                return false; // Queue is full
            }
            queue.offer(customer);
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void serveCustomers(int currentTime) {
        lock.lock();
        try {
            for (int i = 0; i < tellers && !queue.isEmpty(); i++) {
                Customer customer = queue.poll();
                customer.setServed(true);
            }
        } finally {
            lock.unlock();
        }
    }

    public int getQueueSize() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}

