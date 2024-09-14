import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
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
    private final ExecutorService tellerPool;  // For multithreading

    public BankQueue(int numTellers, int maxLength) {
        this.numTellers = numTellers;
        this.queue = new LinkedBlockingQueue<>(maxLength);
        this.tellerPool = Executors.newFixedThreadPool(numTellers);  // Create thread pool
    }

    public void addCustomer(Customer customer) {
        queueLock.lock();
        try {
            // Increment the total number of customers
            totalCustomers.incrementAndGet();

            // If the queue is full and the customer cannot be added, mark the customer as unserved
            if (!queue.offer(customer)) {
                customer.setServed(false);
                totalLeftUnserved.incrementAndGet();
            }
        } finally {
            queueLock.unlock();
        }
    }

    // public void processQueue() {
    //     queueLock.lock();
    //     try {
    //         for (int i = 0; i < numTellers; i++) {
    //             // Get the next customer from the queue
    //             Customer customer = queue.poll();
                
    //             // If a customer is available, process their service time and increment the totals
    //             if (customer != null) {
    //                 totalServiceTime.addAndGet(customer.getServiceTime());
    //                 totalServed.incrementAndGet();
                    
    //                 // Simulate the service time by sleeping for the duration of the service time
    //                 try {
    //                     Thread.sleep(customer.getServiceTime() * 1000L);
    //                 } catch (InterruptedException e) {
    //                     Thread.currentThread().interrupt();
    //                 }
    //             }
    //         }
    //     } finally {
    //         queueLock.unlock();
    //     }
    // }


    public void processQueue() {
        for (int i = 0; i < numTellers; i++) {
            tellerPool.submit(() -> {  // Each teller runs on a separate thread
                try {
                    Customer customer;
                    while ((customer = queue.poll()) != null) {  // Take a customer from the queue
                        totalServiceTime.addAndGet(customer.getServiceTime());
                        totalServed.incrementAndGet();
                        Thread.sleep(customer.getServiceTime() * 1000L);  // Simulate service time
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }

    public void shutdown() {
        tellerPool.shutdown();
        try {
            if (!tellerPool.awaitTermination(60, TimeUnit.SECONDS)) {
                tellerPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            tellerPool.shutdownNow();
            Thread.currentThread().interrupt();
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
