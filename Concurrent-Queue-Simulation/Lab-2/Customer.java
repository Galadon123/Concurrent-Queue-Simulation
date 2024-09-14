import java.util.concurrent.ThreadLocalRandom;

public class Customer {
    private final int arrivalTime;
    private final int serviceTime;
    private boolean served;

    public Customer(int currentTime) {
        this.arrivalTime = currentTime;
        this.serviceTime = ThreadLocalRandom.current().nextInt(120, 601); // Random service time between 60 and 300 seconds
        this.served = false;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServed(boolean served) {
        this.served = served;
    }

    public boolean wasServed() {
        return served;
    }
}
