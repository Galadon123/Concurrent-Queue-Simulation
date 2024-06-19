public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <simulation_time_minutes>");
            return;
        }

        int simulationTimeMinutes = Integer.parseInt(args[0]);
        QueueSimulator simulator = new QueueSimulator(simulationTimeMinutes);

        // Simulate BankQueue
        System.out.println("Simulating BankQueue with 3 tellers and max queue length of 5...");
        simulator.simulateBankQueue(3, 5);

        // Simulate GroceryQueues
        System.out.println("Simulating GroceryQueues with 3 cashiers and max queue length of 2...");
        simulator.simulateGroceryQueues(3, 2);
    }
}
