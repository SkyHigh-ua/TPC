package Lab5;

import java.util.*;
import java.util.concurrent.*;

public class QueueingSystem {
    private static int numChannels = 10;
    private static int maxQueueSize = 10;
    private static int interval = 10;
    private static int totalObjects = 1000;
    private int rejectedObjects = 0;
    private List<Integer> queueLengths = new ArrayList<>();
    private static BlockingQueue<Integer> processedObjects = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        QueueingSystem queue = new QueueingSystem();
        queue.runQueueingSystem(totalObjects, numChannels, maxQueueSize); 
        System.exit(0);
    }

    public QueueingSystemResult runQueueingSystem(int totalObjects, int numChannels, int maxQueueSize) {
        Queue<Object> queue = new LinkedList<>();
        ExecutorService executor = Executors.newFixedThreadPool(numChannels);

        Thread outputThread = new Thread(() -> {
            try {
                for (int i = 0; i < totalObjects; i++) {
                    int processed = processedObjects.take();
                    System.out.println("Processed " + (i + rejectedObjects + 1) + ": Queue length = " + processed);
                    queueLengths.add(processed);
                }

                double averageQueueLength = queueLengths.stream().mapToInt(Integer::intValue).average().orElse(0);
                double rejectionProbability = (double) rejectedObjects / (totalObjects + rejectedObjects);

                System.out.println("Average queue length: " + averageQueueLength);
                System.out.println("Probability of rejection: " + rejectionProbability);
            } catch (InterruptedException e) { }
        });

        outputThread.start();

        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < totalObjects + rejectedObjects; i++) {
                try {
                    Object object = createObject();

                    if (queue.size() < maxQueueSize) {
                        queue.add(object);
                        // System.out.println("Object " + (i+1) + " added. Total objects rejected: " + rejectedObjects);
                    } else if (i < totalObjects + rejectedObjects) {
                        // System.out.println("Queue is full. Object rejected.");
                        rejectedObjects++;
                    }

                    Thread.sleep(getRandomInterval());
                } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt();
                }
            }
        });

        for (int i = 0; i < numChannels; i++) {
            executor.execute(new ConsumerThread(queue));
        }
        
        producerThread.start();

        try {    
            outputThread.join();
        } catch (InterruptedException e) { }
        executor.shutdown();

        return new QueueingSystemResult(
            queueLengths.stream().mapToInt(Integer::intValue).average().orElse(0),
            (double) rejectedObjects / (totalObjects + rejectedObjects)
        );
    }

    private static Object createObject() {
        return new Object();
    }

    private static int getRandomInterval() {
        return new Random().nextInt(interval);
    }

    static class ConsumerThread implements Runnable {
        private Queue<Object> queue;

        ConsumerThread(Queue<Object> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Object object;
                    synchronized (queue) {
                        if (!queue.isEmpty()) {
                            object = queue.poll();
                        } else {
                            continue;
                        }
                        processObject(object);
                        Thread.sleep(getRandomServiceTime());
                        int processed;
                        synchronized (queue) {
                            processed = queue.size();
                        }
                        processedObjects.put(processed);
                    }
                } catch (InterruptedException e) { 
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private static void processObject(Object object) {
        // System.out.println("Object processed");
    }

    private static int getRandomServiceTime() {
        return new Random().nextInt(interval) + 1;
    }
}

class QueueingSystemResult {
    private double averageQueueLength;
    private double rejectionProbability;

    public QueueingSystemResult(double averageQueueLength, double rejectionProbability) {
        this.averageQueueLength = averageQueueLength;
        this.rejectionProbability = rejectionProbability;
    }

    public double getAverageQueueLength() {
        return averageQueueLength;
    }

    public double getRejectionProbability() {
        return rejectionProbability;
    }
}