package Lab3.Task3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Group {
    public int[] grades;
    public String name;
    private Lock[] locks;

    public Group(int size, String name) {
        this.name = name;
        this.grades = new int[size];
        this.locks = new Lock[size];
        for (int i = 0; i < size; i++) {
            this.grades[i] = -1;
            this.locks[i] = new ReentrantLock();
        }
    }

    public int size() {
        return grades.length;
    }

    public int getGrade(int i) {
        return grades[i];
    }

    public void setGrade(int i, int grade) {
        grades[i] = grade;
    }

    public Lock getLock(int i) {
        return locks[i];
    }
}

class Grader implements Runnable {
    private Group[] groups;

    public Grader(Group[] groups) {
        this.groups = groups;
    }

    @Override
    public void run() {
        for (Group group : groups) {
            int size = group.size();
            for (int i = 0; i < size; i++) {
                Lock lock = group.getLock(i);
                if (lock.tryLock()) {
                    try {
                        if (group.getGrade(i) == -1) {
                            int grade = (int) (Math.random() * 101);
                            group.setGrade(i, grade);
                            System.out.println("Grade " + grade + " assigned to student " + i + " in group "
                                    + group.name + " by thread " + Thread.currentThread().threadId() + ".");
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}

public class ElectronicJournal {
    public static void main(String[] args) {
        int groupSize = 30;
        int numberOfGroups = 3;

        Group[] groups = new Group[numberOfGroups];
        for (int i = 0; i < numberOfGroups; i++) {
            groups[i] = new Group(groupSize, "G" + i);
        }

        Grader lecturer = new Grader(groups);
        Grader grader1 = new Grader(groups);
        Grader grader2 = new Grader(groups);
        Grader grader3 = new Grader(groups);
        Thread graderThread0 = new Thread(lecturer);
        Thread graderThread1 = new Thread(grader1);
        Thread graderThread2 = new Thread(grader2);
        Thread graderThread3 = new Thread(grader3);
        graderThread0.start();
        graderThread1.start();
        graderThread2.start();
        graderThread3.start();
    }
}
