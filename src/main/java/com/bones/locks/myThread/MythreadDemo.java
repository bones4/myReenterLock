package com.bones.locks.myThread;

public class MythreadDemo {
    static void log(String str) {
        System.out.println(str);
    }
    public static void main(String[] args) throws InterruptedException {

        /*
         * 线
         * 程new的时候用工厂类注册到map管理起来，
         * 可通过工厂类暂停某个线程，
         *暂停的方法是在后台控制重入锁 加锁，
         * */
        MyThread myThread = MyThreadFactory.create("002", new Task1());
        myThread.start();
        Thread.sleep(3000);
        MyThreadFactory.unpark("002");

    }

    private static class Task1 implements Runnable {
        @Override
        public void run() {
            log("002say:1");
            MyThreadFactory.park("002");
            for (int i = 2; i < 10; i++) {
                log("002say:" + i);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
