package com.bones.locks.myReenterLock;

import com.bones.locks.myThread.MyThread;

/**
 *
 */
public class ReenterLockDemo implements Runnable {
    //public static ReentrantLock lock = new ReentrantLock();
    public static MyReentrantLock  lock = new MyReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " start");
        lock.lock();
        System.out.println(name + " get lock and running");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lock.unlock();
        System.out.println(name + " end");
    }

    public static void main(String args[]) throws InterruptedException {
        System.out.println("threadMain start");
        MyThread thread2 = new MyThread("thread2",new ReenterLockDemo());
        MyThread thread1 = new MyThread("thread1",new ReenterLockDemo());


        thread1.start();
        thread2.start();
        System.out.println("threadMain end");
    }

}
