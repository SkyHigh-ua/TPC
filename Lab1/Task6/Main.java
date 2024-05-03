package Task6;

public class Main {
    public static void main(String[] args) {
        Counter counter = new Counter();

        Thread incrementThread = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        });

        Thread decrementThread = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                counter.decrement();
            }
        });

        incrementThread.start();
        decrementThread.start();

        try {
            incrementThread.join();
            decrementThread.join();
        } catch (InterruptedException e) {
        }

        System.out.println("Final Count: " + counter.getCount());
    }
}
