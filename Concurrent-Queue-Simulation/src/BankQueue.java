import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BankQueue {
    private final int numTellers;
    private final LinkedBlockingQueue<Customer> queue;
    private final AtomicInteger totalServed = new AtomicInteger(0);
    private final AtomicInteger totalLeftUnserved = new AtomicInteger(0);
    private final AtomicInteger totalCustomers = new AtomicInteger(0);
    private final AtomicInteger totalServiceTime = new AtomicInteger(0);

    public BankQueue(int numTellers, int maxLength) {
        this.numTellers = numTellers;
        this.queue = new LinkedBlockingQueue<>(maxLength);
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
