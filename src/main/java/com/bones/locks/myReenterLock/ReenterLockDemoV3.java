package com.bones.locks.myReenterLock;

import com.bones.locks.myThread.MyThread;

public class ReenterLockDemoV3 implements Runnable {
    public static void main(String args[]) throws InterruptedException {
        //测试第2版本，100个线程，花费1096毫秒，
        //testV2();

        //测试第3版本，100个线程，花费196毫秒，
        testV3();

    }

    private static void testV3() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("threadMain start");
        for (int i = 0; i < 2; i++) {
            MyThread thread = new MyThread("thread" + i, new ReenterLockDemoV3());
            thread.start();
        }
        System.out.println("threadMain end");
        while (ReenterLockDemoV3.lock.head.get().waitState==-1){
        }
        Thread.sleep(10);
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("花费时间： " + endTime);
    }

    private static void testV2() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("threadMain start");
        for (int i = 0; i < 100; i++) {
            MyThread thread = new MyThread("thread" + i, new ReenterLockDemoV2());
            thread.start();
        }


        System.out.println("threadMain end");
        while (ReenterLockDemoV2.lock.head.waitState==-1){
        }
        Thread.sleep(10);
        long endTime = System.currentTimeMillis() - startTime;
        System.out.println("花费时间： " + endTime);
    }

    public static MyReentrantLockV3  lock = new MyReentrantLockV3();

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        System.out.println(name + " start");
        lock.lock();
        System.out.println(name + " get lock and running");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " end");
        lock.unlock();

    }

}
