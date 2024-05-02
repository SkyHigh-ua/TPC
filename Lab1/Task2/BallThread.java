package Task2;

public class BallThread extends Thread {
    private Ball b;
    private boolean working = true;

    public BallThread(Ball ball) {
        b = ball;
    }

    @Override
    public void run() {
        try {
            while (working) {
                b.move();
                System.out.println("Thread name = " + Thread.currentThread().getName());
                Thread.sleep(5);
                working = !b.isBallInHole();
            }
            System.out.println(Thread.currentThread().getName() + " ended");
        } catch (InterruptedException ex) {}
    }

    public void stopThread() {
        working = false;
    }
}