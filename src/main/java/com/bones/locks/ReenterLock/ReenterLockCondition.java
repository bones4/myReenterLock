package com.bones.locks.ReenterLock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Semaphore = lock.newCondition
 * 在lock内部提供等待await和唤醒signal方法
 *这个等待的wait得在外部线程的同一把锁的lock，unlock代码之间，
 * 也就是说这把锁得lock2次，unlock2次，所以叫重入锁，
 */
public class ReenterLockCondition implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();

    @Override
    public void run() {

        try {
            lock.lock();
            condition.await();
            System.out.println("Thread is going on");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public static void main(String args[]) throws InterruptedException {
        ReenterLockCondition reenterLockCondition = new ReenterLockCondition();
        Thread thread1 = new Thread(reenterLockCondition);
        thread1.start();
        System.out.println("˯��2����");
        Thread.sleep(2000);
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
