import java.util.concurrent.ThreadLocalRandom;

public class Customer {
    private final long arrivalTime;
    private final int serviceTime;  // the service time of the customer
    private boolean served;         // whether the customer has been served or not

    public Customer(long arrivalTime) {
        this.arrivalTime = arrivalTime;
        // Randomly generate a service time between 60 and 300 minutes 
        this.serviceTime = ThreadLocalRandom.current().nextInt(60, 301);
        this.served = true;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public boolean isServed() {
        return served;
    }

    public void setServed(boolean served) {
        this.served = served;
    }
}
