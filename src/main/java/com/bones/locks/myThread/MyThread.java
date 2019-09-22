package com.bones.locks.myThread;


import java.util.concurrent.locks.ReentrantLock;

public class MyThread extends   Thread  {
    private   ReentrantLock lock = new ReentrantLock();
    public MyThread(Runnable runnable) {
        super(runnable);
        MyThreadFactory.create(this.getName(),this);
    }
    public MyThread(Runnable runnable,boolean isdaemon) {
        super(runnable);
    }
    public MyThread(String name,Runnable runnable) {
        super(runnable);
        MyThreadFactory.create(name,this);
        this.setName(name);
    }

/********************************************/
    public ReentrantLock getLock() {
        return lock;
    }

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }
}
