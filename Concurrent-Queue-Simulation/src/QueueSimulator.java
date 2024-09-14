import java.util.concurrent.ThreadLocalRandom;

public class QueueSimulator {
    private final int simulationTimeMinutes;

    public QueueSimulator(int simulationTimeMinutes) {
        this.simulationTimeMinutes = simulationTimeMinutes;
    }

    public void simulateBankQueue(int numTellers, int maxLength) {
        
        // Creating a bank queue with the specified number of tellers and maximum queue length
        BankQueue bankQueue = new BankQueue(numTellers, maxLength);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + simulationTimeMinutes * 60 * 1000L;

        while (System.currentTimeMillis() < endTime) {
            // Create a new customer and add it to the bank queue
            long currentTime = System.currentTimeMillis();
            Customer customer = new Customer(currentTime);
            bankQueue.addCustomer(customer);
            
            // Process the bank queue
            bankQueue.processQueue();

            // Sleep for a random time between 20 and 60 seconds
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(20, 61) * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Bank Queue Simulation Results:");
        System.out.println("Total customers: " + bankQueue.getTotalCustomers());
        System.out.println("Total customers served: " + bankQueue.getTotalServed());
        System.out.println("Total customers left unserved: " + bankQueue.getTotalLeftUnserved());
        System.out.println("Average service time: " + (bankQueue.getTotalServiceTime() / (double) bankQueue.getTotalServed()));
    }

    public void simulateGroceryQueues(int numQueues, int maxLength) {
        GroceryQueues groceryQueues = new GroceryQueues(numQueues, maxLength);
        long startTime = System.currentTimeMillis();
        long endTime = startTime + simulationTimeMinutes * 60 * 1000L;

        while (System.currentTimeMillis() < endTime) {
            long currentTime = System.currentTimeMillis();
            Customer customer = new Customer(currentTime);
            groceryQueues.addCustomer(customer);
            groceryQueues.processQueues();

            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(20, 61) * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("Grocery Queue Simulation Results:");
        System.out.println("Total customers: " + groceryQueues.getTotalCustomers());
        System.out.println("Total customers served: " + groceryQueues.getTotalServed());
        System.out.println("Total customers left unserved: " + groceryQueues.getTotalLeftUnserved());
        System.out.println("Average service time: " + (groceryQueues.getTotalServiceTime() / (double) groceryQueues.getTotalServed()));
    }
}
 