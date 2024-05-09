package Lab3.Task1.Fixed;

public class AsynchBankTestSyncBlock {
    public static final int NACCOUNTS = 10;
    public static final int INITIAL_BALANCE = 10000;

    public static void main(String[] args) {
        BankWith bank = new BankWith(NACCOUNTS, INITIAL_BALANCE);
        for (int i = 0; i < NACCOUNTS; i++) {
            TransferThreadWith t = new TransferThreadWith(bank, i, INITIAL_BALANCE);
            t.setPriority(Thread.NORM_PRIORITY + i % 2);
            t.start();
        }
    }
}

class BankWith {
    public static final int NTEST = 10000;
    private final int[] accounts;
    private long ntransacts = 0;

    public BankWith(int n, int initialBalance) {
        accounts = new int[n];
        for (int i = 0; i < accounts.length; i++)
            accounts[i] = initialBalance;
    }

    public void transfer(int from, int to, int amount) {
        synchronized (this) {
            accounts[from] -= amount;
            accounts[to] += amount;
            ntransacts++;
            if (ntransacts % NTEST == 0)
                test();
        }
    }

    public void test() {
        synchronized (this) {
            int sum = 0;
            for (int account : accounts)
                sum += account;
            System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
        }
    }

    public int size() {
        return accounts.length;
    }
}

class TransferThreadWith extends Thread {
    private BankWith bank;
    private int fromAccount;
    private int maxAmount;
    private static final int REPS = 1000;

    public TransferThreadWith(BankWith b, int from, int max) {
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

