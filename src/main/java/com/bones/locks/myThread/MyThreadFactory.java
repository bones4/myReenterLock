package com.bones.locks.myThread;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class MyThreadFactory {

    public static ConcurrentHashMap<String, MyThread> threadMap
            = new ConcurrentHashMap<>();
    public static BlockingQueue<HashMap<String, String>> cmdQueue
            = new ArrayBlockingQueue<HashMap<String, String>>(1);

    public static MyThread create(String Name, Runnable runnable) {
        MyThreadFactoryinit();
        MyThread myThread = new MyThread(runnable,false);
        myThread.setName(Name);
        threadMap.put(Name, myThread);
        return myThread;
    }
    private static String _daemonName="1";

    private static class cmdState{
        private static String _name="name";
        private static String _cmd="cmd";
        private static String _cmd_lock="lock";
        private static String _cmd_unlock="unlock";
    }

    private static void MyThreadFactoryinit() {
        if (getThread(_daemonName) == null) {
            MyThread myThread2 = new MyThread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        HashMap<String, String> cmd = null;
                        try {
                            cmd = MyThreadFactory.cmdQueue.take();
                            //System.out.println("tack."+cmd.toString() );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if(cmd==null) continue;
                        if (cmd.get(cmdState._cmd).equals(cmdState._cmd_lock)) {
                           MyThreadFactory.getThread(cmd.get(cmdState._name))
                                   .getLock().lock();
                        }
                        else if (cmd.get(cmdState._cmd).equals(cmdState._cmd_unlock)) {
                            MyThreadFactory.getThread(cmd.get(cmdState._name))
                                    .getLock().unlock();
                        }
                    }
                }
            },true);
            threadMap.put(_daemonName, myThread2);
            myThread2.setDaemon(true);
            myThread2.start();
        }
    }

    public static MyThread getThread(String name) {
        return threadMap.get(name);
    }

    public static void park(MyThread t) {

        HashMap cmd = new HashMap<>();
        cmd.put(cmdState._name, t.getName());
        cmd.put(cmdState._cmd, cmdState._cmd_lock);
        try {
            cmdQueue.put(cmd);
            //System.out.println(t.getName()+" park "+cmd.toString()+" size="+cmdQueue.size());
        } catch (InterruptedException e) {
            System.out.println("cmdQueue put exception");
            e.printStackTrace();
        }
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            System.out.println("cmdQueue put sleep");
            e.printStackTrace();
        }
        t.getLock().lock();
    }

    public static void unpark(MyThread t) {
        HashMap cmd = new HashMap<>();
        cmd.put(cmdState._name, t.getName());
        cmd.put(cmdState._cmd, cmdState._cmd_unlock);
        try {
            cmdQueue.put(cmd);
        } catch (InterruptedException e) {
            System.out.println("cmdQueue.put.exception");
            e.printStackTrace();
        }
    }

    /**********************extend*******************************************/
    public static void park(String name) {
        park(MyThreadFactory.getThread(name));
    }

    public static void unpark(String name) {
        unpark(MyThreadFactory.getThread(name));
    }
}
