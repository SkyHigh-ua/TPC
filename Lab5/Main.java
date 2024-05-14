package Lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    private static int numChannels = 10;
    private static int maxQueueSize = 10;
    private static int totalObjects = 1000;
    private static final int NUM_SIMULATIONS = 20;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(NUM_SIMULATIONS);
        List<Future<QueueingSystemResult>> futures = new ArrayList<>();

        for (int i = 0; i < NUM_SIMULATIONS; i++) {
            QueueingSystem queue = new QueueingSystem();
            Future<QueueingSystemResult> future = executor.submit(() -> queue.runQueueingSystem(totalObjects, numChannels, maxQueueSize));

            futures.add(future);
        }

        double totalAverageQueueLength = 0;
        double totalRejectionProbability = 0;
        for (Future<QueueingSystemResult> future : futures) {
            try {
                QueueingSystemResult result = future.get();
                totalAverageQueueLength += result.getAverageQueueLength();
                totalRejectionProbability += result.getRejectionProbability();
            } catch (InterruptedException | ExecutionException e) { }
        }
        executor.shutdown();
        
        double averageAverageQueueLength = totalAverageQueueLength / NUM_SIMULATIONS;
        double averageRejectionProbability = totalRejectionProbability / NUM_SIMULATIONS;

        System.out.println("Average of " + NUM_SIMULATIONS + " simulations:");
        System.out.println("Average Queue Length: " + averageAverageQueueLength);
        System.out.println("Average Rejection Probability: " + averageRejectionProbability);
        System.exit(0);
    }   
}

