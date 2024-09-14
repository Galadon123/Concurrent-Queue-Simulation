import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;

public class QueueSimulator {
    private final int simulationTime;  // Total simulation time in seconds
    private final BankQueue bankQueue;
    private final GroceryQueues groceryQueues;
    private int currentTime;
    
    private final List<Customer> bankCustomers = new ArrayList<>();
    private final List<Customer> groceryCustomers = new ArrayList<>();

    private int bankArrivals = 0;
    private int groceryArrivals = 0;
    private int bankCustomersLeft = 0;
    private int groceryCustomersLeft = 0;

    public QueueSimulator(int simulationTime, BankQueue bankQueue, GroceryQueues groceryQueues) {
        this.simulationTime = simulationTime;
        this.bankQueue = bankQueue;
        this.groceryQueues = groceryQueues;
        this.currentTime = 0;
    }

    public void runSimulation() {
        int nextBankArrival = getRandomArrivalTime();
        int nextGroceryArrival = getRandomArrivalTime();
    
        while (currentTime < simulationTime) {
            currentTime++;
    
            // BankQueue customer arrival
            if (currentTime >= nextBankArrival) {
                bankArrivals++;
                Customer customer = new Customer(currentTime);
                if (!bankQueue.addCustomer(customer)) {
                    bankCustomersLeft++;
                } else {
                    bankCustomers.add(customer);
                }
                nextBankArrival = currentTime + getRandomArrivalTime();
            }
    
            // GroceryQueues customer arrival
            if (currentTime >= nextGroceryArrival) {
                groceryArrivals++;
                Customer customer = new Customer(currentTime);
                if (!groceryQueues.addCustomer(customer, currentTime)) {
                    groceryCustomersLeft++;
                } else {
                    groceryCustomers.add(customer);
                }
                nextGroceryArrival = currentTime + getRandomArrivalTime();
            }
    
            // Serve customers in both queues
            bankQueue.serveCustomers(currentTime);
            groceryQueues.serveCustomers(currentTime);
    
            // Add delay to simulate real time
            try {
                Thread.sleep(1000);  // Sleep for 1 second (simulate real time delay)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        outputResults();
    }    

    // Generate random time for the next customer arrival (between 20 and 60 seconds)
    private int getRandomArrivalTime() {
        return ThreadLocalRandom.current().nextInt(20, 61);
    }
    
    private void outputResults() {
        // BankQueue stats
        int totalBankServed = (int) bankCustomers.stream().filter(Customer::wasServed).count();
        int totalGroceryServed = (int) groceryCustomers.stream().filter(Customer::wasServed).count();
        
        double avgBankServiceTime = bankCustomers.stream().filter(Customer::wasServed)
                .mapToDouble(c -> c.getServiceTime()).average().orElse(0);
        double avgGroceryServiceTime = groceryCustomers.stream().filter(Customer::wasServed)
                .mapToDouble(c -> c.getServiceTime()).average().orElse(0);

        System.out.println("=== BankQueue Results ===");
        System.out.println("Total Customers Arrived: " + bankArrivals);
        System.out.println("Total Customers Served: " + totalBankServed);
        System.out.println("Total Customers Left Without Service: " + bankCustomersLeft);
        System.out.printf("Average Service Time: %.2f seconds\n", avgBankServiceTime);

        System.out.println("\n=== GroceryQueues Results ===");
        System.out.println("Total Customers Arrived: " + groceryArrivals);
        System.out.println("Total Customers Served: " + totalGroceryServed);
        System.out.println("Total Customers Left Without Service: " + groceryCustomersLeft);
        System.out.printf("Average Service Time: %.2f seconds\n", avgGroceryServiceTime);
    }
}
