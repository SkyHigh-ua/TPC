package Lab3.Task2;

import java.util.*;

class Drop {
    private List<Integer> data;
    private boolean empty = true;
    private int maxSize;

    public Drop(int maxSize) {
        this.maxSize = maxSize;
        data = new ArrayList<>(maxSize);
    }

    public synchronized int take() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        int item = data.remove(0);
        if (data.isEmpty()) {
            empty = true;
        }
        notifyAll();
        return item;
    }

    public synchronized void put(int newData) {
        while (data.size() >= maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        data.add(newData);
        System.out.println("Data created: " + newData + "; Array size: " + data.size());
        empty = false;
        notifyAll();
    }
}

class Producer implements Runnable {
    private Drop drop;

    public Producer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        Random random = new Random();
        while (true) {
            int importantInfo = random.nextInt(1000);
            drop.put(importantInfo);
        }
    }
}

class Consumer implements Runnable {
    private Drop drop;

    public Consumer(Drop drop) {
        this.drop = drop;
    }

    public void run() {
        while (true) {
            int data = drop.take();
            System.out.println("Data received: " + data);
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        int maxSize = 10;
        Drop drop = new Drop(maxSize);
        (new Thread(new Producer(drop))).start();
        (new Thread(new Consumer(drop))).start();
    }
}