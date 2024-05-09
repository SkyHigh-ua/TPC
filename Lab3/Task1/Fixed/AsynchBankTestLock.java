package Lab3.Task1.Fixed;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AsynchBankTestLock {
    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
        Bank bank = new Bank(NACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < NACCOUNTS; i++) {
            TransferThreadWithLock t = new TransferThreadWithLock(bank, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }
}

class Bank {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;
    private Lock lock = new ReentrantLock();

    public Bank(int n, int initialBalance) {
        accounts = new int[n];
        for (int i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
    }

    public void transfer(int from, int to, int amount) {
        lock.lock();
        try {
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            if (ntransacts % NTEST == 0)
                test();
        } finally {
            lock.unlock();
        }
    }

    public void test() {
        lock.lock();
        try {
            int sum = 0;
            for (int account : accounts)
                sum += account;
            System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return accounts.length;
    }
}

class TransferThreadWithLock extends Thread {
    private Bank bank;
    private int fromAccount;
    private int maxAmount;
    private static final int REPS = 1000;

    public TransferThreadWithLock(Bank b, int from, int max) {
        bank = b;
        fromAccount = from;
        maxAmount = max;
    }

    @Override
    public void run() {
        while (true) {
            for (int i = 0; i < REPS; i++) {
                int toAccount = (int) (bank.size() * Math.random());
                int amount = (int) (maxAmount * Math.random() / REPS);
                bank.transfer(fromAccount, toAccount, amount);
            }
        }
    }
}

