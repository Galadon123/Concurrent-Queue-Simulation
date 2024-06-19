import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GroceryQueues {
    private final LinkedBlockingQueue<Customer>[] queues;
    private final Lock[] queueLocks;
    private final int maxQueueLength;
    private final AtomicInteger totalServed = new AtomicInteger(0);
    private final AtomicInteger totalLeftUnserved = new AtomicInteger(0);
    private final AtomicInteger totalCustomers = new AtomicInteger(0);
    private final AtomicInteger totalServiceTime = new AtomicInteger(0);

    public GroceryQueues(int numQueues, int maxQueueLength) {
        this.queues = new LinkedBlockingQueue[numQueues];
        this.queueLocks = new ReentrantLock[numQueues];
        for (int i = 0; i < numQueues; i++) {
            queues[i] = new LinkedBlockingQueue<>(maxQueueLength);
            queueLocks[i] = new ReentrantLock();
        }
        this.maxQueueLength = maxQueueLength;
    }

    public void addCustomer(Customer customer) {
        totalCustomers.incrementAndGet();
        int shortestQueueIndex = -1;
        int shortestQueueSize = Integer.MAX_VALUE;

        for (int i = 0; i < queues.length; i++) {
            queueLocks[i].lock();
            try {
                if (queues[i].size() < shortestQueueSize) {
                    shortestQueueIndex = i;
                    shortestQueueSize = queues[i].size();
                }
            } finally {
                queueLocks[i].unlock();
            }
        }

        if (shortestQueueIndex != -1 && shortestQueueSize < maxQueueLength) {
            queueLocks[shortestQueueIndex].lock();
            try {
                queues[shortestQueueIndex].offer(customer);
            } finally {
                queueLocks[shortestQueueIndex].unlock();
            }
        } else {
            customer.setServed(false);
            totalLeftUnserved.incrementAndGet();
        }
    }

    public void processQueues() {
        for (int i = 0; i < queues.length; i++) {
            queueLocks[i].lock();
            try {
                Customer customer = queues[i].poll();
                if (customer != null) {
                    totalServiceTime.addAndGet(customer.getServiceTime());
                    totalServed.incrementAndGet();
                    try {
                        Thread.sleep(customer.getServiceTime() * 1000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            } finally {
                queueLocks[i].unlock();
            }
        }
    }

    public int getTotalServed() {
        return totalServed.get();
    }

    public int getTotalLeftUnserved() {
        return totalLeftUnserved.get();
    }

    public int getTotalCustomers() {
        return totalCustomers.get();
    }

    public int getTotalServiceTime() {
        return totalServiceTime.get();
    }
}
