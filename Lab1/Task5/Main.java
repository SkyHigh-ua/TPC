package Task5;

public class Main {
    public static void main(String[] args) {
        int rows = 100;
        int symbolsPerRow = 50;

        Object lock = new Object();

        for (int i = 0; i < rows; i++) {
            PrintThread thread1 = new PrintThread('-', symbolsPerRow, lock);
            PrintThread thread2 = new PrintThread('|', symbolsPerRow, lock);

            
            // thread1.start();
            // thread2.start();

            try {
                thread1.start();
                synchronized (lock) {
                    lock.wait();
                }
                thread2.start();
                thread2.join();
            } catch (InterruptedException e) {}

            System.out.println();
        }
    }
}

