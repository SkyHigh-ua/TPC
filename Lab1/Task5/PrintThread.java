package Task5;

public class PrintThread extends Thread {
    private char symbol;
    private int count;
    private Object lock;

    public PrintThread(char symbol, int count, Object lock) {
        this.symbol = symbol;
        this.count = count;
        this.lock = lock;
    }

    @Override
    public void run() {
        synchronized (lock) {
            for (int i = 0; i < count; i++) {
                System.out.print(symbol);
                try {
                    lock.notify();
                    if (i < count - 1) {
                        lock.wait();
                    }
                } catch (InterruptedException e) {}
            }
        }
    }

    // @Override
    // public void run() {
    //     for (int i = 0; i < count; i++) {
    //         System.out.print(symbol);
    //     }
    // }
}
