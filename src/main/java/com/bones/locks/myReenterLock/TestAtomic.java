package com.bones.locks.myReenterLock;

import java.util.concurrent.atomic.AtomicInteger;

/*
* 结论， 把sys
*
* */
public class TestAtomic {


    public static Object lock=new Object();
    public static volatile long starttime=0;
    public static  long endtime=0;
    public static Integer count=0;
    public static volatile AtomicInteger countAtomic=new AtomicInteger(0);
    public static void main(String args[]) throws InterruptedException {

        starttime= System.currentTimeMillis();
        int i=1000;
        Thread[] threads = new Thread[i];
        for (int j = 0; j < i; j++) {
            //用synchronaized耗时280ms
            //threads[j] = new Thread(new Thread2());
            //用atomic耗时147ms  特殊场景下确实高效了一点点
            threads[j] = new Thread(new ThreadAtomic());
        }
        for (int j = 0; j < i; j++) {
            threads[j].start();
        }


    }

    static  class Thread2 implements Runnable {

        @Override
        public void run() {
            for (int i = 0; i < 1000; i++) {
                synchronized (lock){
                    count++;
                }
            }
            TestAtomic.endtime =System.currentTimeMillis() - TestAtomic.starttime;
            System.out.println("endtime="+ TestAtomic.endtime );


        }
    }
    static class ThreadAtomic implements Runnable {

        @Override
        public void run() {

            for (int i = 0; i < 1000; i++) {
                for (; ; ) {
                    int countNow = countAtomic.get();
                    if (countAtomic.compareAndSet(countNow, countNow + 1)) {
                        break;
                    }
                }
            }
            TestAtomic.endtime =System.currentTimeMillis() - TestAtomic.starttime;
            System.out.println("endtime="+ TestAtomic.endtime );
        }
    }


}
