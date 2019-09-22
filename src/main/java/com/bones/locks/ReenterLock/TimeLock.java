package com.bones.locks.ReenterLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * lock.tryLock(5) 锁申请等待限时
 * 线程任务里超过5秒没拿到锁就输出失败
 * 2个线程获取锁，第1个拿锁成功后睡6秒，第2个线程拿锁失败
 */
public class TimeLock implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();

    @Override
    public void run() {
        try {
            if (lock.tryLock(5, TimeUnit.SECONDS)) {
                System.out.println(Thread.currentThread().getName());
                System.out.println("get lock success");
                Thread.sleep(6000);
            } else {
                System.out.println(Thread.currentThread().getName());
                System.out.println("get lock failed");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * ������ȴ���ʱtryLock
     * @param args
     */
    public static void main(String args[]) {
        TimeLock timeLock = new TimeLock();
        Thread thread1 = new Thread(timeLock);
        Thread thread2 = new Thread(timeLock);

        thread1.start();
        thread2.start();
    }
}
