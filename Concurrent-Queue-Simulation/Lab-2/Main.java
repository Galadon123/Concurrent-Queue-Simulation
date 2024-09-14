public class Main {
    public static void main(String[] args) {
        int simulationTimeInMinutes = 2; // 2 hours simulation
        int tellers = 3;
        int cashiers = 3;
        int maxBankQueueSize = 5;
        int maxGroceryQueueSize = 2;

        BankQueue bankQueue = new BankQueue(tellers, maxBankQueueSize);
        GroceryQueues groceryQueues = new GroceryQueues(cashiers, maxGroceryQueueSize);

        QueueSimulator simulator = new QueueSimulator(simulationTimeInMinutes * 60, bankQueue, groceryQueues);
        simulator.runSimulation();
    }
}
