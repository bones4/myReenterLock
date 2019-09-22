package com.bones.locks.myReenterLock;

import com.bones.locks.myThread.MyThread;
import com.bones.locks.myThread.MyThreadFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

//ReentrantLock
public class MyReentrantLockV3 {
    public static Object object = new Object();
    //public  int state=0;
    public volatile AtomicInteger state = new AtomicInteger(0);

    public volatile AtomicReference<Node> head = new AtomicReference<>(new Node());
    public volatile AtomicReference<Node> tail = head;

    public class Node {
        volatile Node last = null;
        volatile Node next = null;
        volatile int waitState = 0;
        volatile MyThread thread;
    }

    public void lock() {
        if (state.compareAndSet(0, 1)) {
        }
        else {
            acquire(1);
        }

    }

    private void acquire(int arg) {
        String thread = null;
        Node now = new Node();
        thread = Thread.currentThread().getName();
        now.thread = MyThreadFactory.getThread(thread);
        enq(now);
        MyThreadFactory.park(thread);
    }

    //把结果加入结尾
    private Node enq(Node now) {
        for (; ; ) {
            Node tailOld = tail.get();
            tailOld.next = now;
            now.last = tailOld;
            tailOld.waitState = -1;
            if (tail.compareAndSet(tailOld,now)) {
                return tailOld;
            }
        }
    }


    public void unlock() {
        if (state.get() > 0) {//如果需要唤醒
            //从头部开始唤醒
            Node headNode = head.get();
            if (headNode.waitState == -1) {
                Node current = headNode.next;
                MyThreadFactory.unpark(current.thread.getName());
                /*
                 * 唤醒成功从链表删除该节点
                 * 如果有节点3，则把head.next指向节点3
                 * 如果没有节点3，head.next=null
                 * */
                if (current.next != null) {
                    headNode.next = current.next;
                    headNode.waitState = -1;
                } else {
                    headNode.next = null;
                    headNode.waitState = 0;
                }
                current = null;
            }
        }

    }

}
