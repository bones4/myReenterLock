package com.bones.locks.myReenterLock;

import com.bones.locks.myThread.MyThread;

/**
 *
 */
public class ReenterLockDemoV2 implements Runnable {
    //public static ReentrantLock lock = new ReentrantLock();
    public static MyReentrantLockV2  lock = new MyReentrantLockV2();

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " start");
        lock.lock();
        System.out.println(name + " get lock and running");

        System.out.println(name + " end");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();

    }

    public static void main(String args[]) throws InterruptedException {
        System.out.println("threadMain start");
        MyThread thread1 = new MyThread("thread1",new ReenterLockDemoV2());
        MyThread thread2 = new MyThread("thread2",new ReenterLockDemoV2());
        MyThread thread3 = new MyThread("thread3",new ReenterLockDemoV2());
        thread1.start();
        Thread.sleep(10);
        thread2.start();
        thread3.start();

        System.out.println("threadMain end");
    }

}
