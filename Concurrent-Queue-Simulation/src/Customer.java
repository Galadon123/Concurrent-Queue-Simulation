import java.util.concurrent.ThreadLocalRandom;

public class Customer {
    private final long arrivalTime;
    private final int serviceTime;
    private boolean served;

    public Customer(long arrivalTime) {
        this.arrivalTime = arrivalTime;
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
