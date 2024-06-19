import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankQueue {
    private final int numTellers;
    private final LinkedBlockingQueue<Customer> queue;
    private final AtomicInteger totalServed = new AtomicInteger(0);
    private final AtomicInteger totalLeftUnserved = new AtomicInteger(0);
    private final AtomicInteger totalCustomers = new AtomicInteger(0);
    private final AtomicInteger totalServiceTime = new AtomicInteger(0);
    private final Lock queueLock = new ReentrantLock();

    public BankQueue(int numTellers, int maxLength) {
        this.numTellers = numTellers;
        this.queue = new LinkedBlockingQueue<>(maxLength);
    }

    public void addCustomer(Customer customer) {
        queueLock.lock();
        try {
            totalCustomers.incrementAndGet();
            if (!queue.offer(customer)) {
                customer.setServed(false);
                totalLeftUnserved.incrementAndGet();
            }
        } finally {
            queueLock.unlock();
        }
    }

    public void processQueue() {
        queueLock.lock();
        try {
            for (int i = 0; i < numTellers; i++) {
                Customer customer = queue.poll();
                if (customer != null) {
                    totalServiceTime.addAndGet(customer.getServiceTime());
                    totalServed.incrementAndGet();
                    try {
                        Thread.sleep(customer.getServiceTime() * 1000L);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        } finally {
            queueLock.unlock();
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
