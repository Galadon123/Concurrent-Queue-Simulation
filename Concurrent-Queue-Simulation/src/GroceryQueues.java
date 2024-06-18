import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class GroceryQueues {
    private final LinkedBlockingQueue<Customer>[] queues;
    private final int maxQueueLength;
    private final AtomicInteger totalServed = new AtomicInteger(0);
    private final AtomicInteger totalLeftUnserved = new AtomicInteger(0);
    private final AtomicInteger totalCustomers = new AtomicInteger(0);
    private final AtomicInteger totalServiceTime = new AtomicInteger(0);

    public GroceryQueues(int numQueues, int maxQueueLength) {
        this.queues = new LinkedBlockingQueue[numQueues];
        for (int i = 0; i < numQueues; i++) {
            queues[i] = new LinkedBlockingQueue<>(maxQueueLength);
        }
        this.maxQueueLength = maxQueueLength;
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
