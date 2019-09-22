package com.bones.locks.ReenterLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 *  lock.lock, lock.unlock
 * 重入锁 简单例子，
 * 跟synchronized作用相同
 * 为什么叫重入锁，提供等待和唤醒机制，
 * 这个等待的wait得在外部线程的同一把锁的lock，unlock代码之间，
 * 也就是说这把锁得lock2次，unlock2次，所以叫重入锁，
 */
public class ReenterLock implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();
    //public static MyReentrantLock  lock = new MyReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        System.out.println("thread2.run");
        for (int j = 0; j < 1000000; j++) {
            lock.lock();
            try {
                i++;
            } finally {
                lock.unlock();
            }
        }
        System.out.println("thread2.end");
    }

    public static void main(String args[]) throws InterruptedException {
        ReenterLock reenterLock = new ReenterLock();
        Thread thread2 = new Thread(reenterLock);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread1.run");
                lock.lock();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.unlock();
                System.out.println("thread1.end");
            }
        });

        thread1.start();
        Thread.sleep(100);
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(i);
    }

}
