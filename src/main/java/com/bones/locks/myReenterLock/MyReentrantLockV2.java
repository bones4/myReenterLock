package com.bones.locks.myReenterLock;

import com.bones.locks.myThread.MyThread;
import com.bones.locks.myThread.MyThreadFactory;

//ReentrantLock
public class MyReentrantLockV2 {
    public static Object object=new Object();
     public  int state=0;
     public  Node head =new Node();
     public  Node tail = head;
     public  class Node{
         Node last=null;
         Node next=null;
         int waitState=0;
         MyThread thread;
     }
     public  void  lock(){

         if(state==0){
             synchronized (object) {
                 state++;
             }
         }
         else if (state>0){//也就是state原始>0，锁已经被持有的状态，
             String thread=null;
             synchronized (object) {
                 state++;
                 Node now = new Node();
                 thread = Thread.currentThread().getName();
                 now.thread = MyThreadFactory.getThread(thread);
                 Node tailOld = tail;
                 tailOld.next = now;
                 now.last = tailOld;

                 tailOld.waitState = -1;
                 tail = now;
             }
             MyThreadFactory.park(thread);
         }

     }

     public   void unlock(){
         if(state>0) {//如果需要唤醒
             //从头部开始唤醒
             if (head.waitState == -1) {
                 synchronized (object) {
                     Node current = head.next;
                     MyThreadFactory.unpark(current.thread.getName());
                     /*
                      * 唤醒成功从链表删除该节点
                      * 如果有节点3，则把head.next指向节点3
                      * 如果没有节点3，head.next=null
                      * */

                     if (current.next != null) {
                         head.next = current.next;
                         head.waitState = -1;
                     } else {
                         head.next = null;
                         head.waitState = 0;
                     }
                     current = null;
                     //state-1
                     state--;
                 }
             }
         }

     }
}
