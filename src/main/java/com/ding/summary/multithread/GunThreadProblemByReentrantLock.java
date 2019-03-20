package com.ding.summary.multithread;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者问题
 * 采用Java 多线程技术（例如wait和notify），设计实现一个符合生产者和消费者问题的程序。对一个对象（枪膛）进行操作，
 * 其最大容量是20颗子弹。生产者线程是一个压入线程，它不断向枪膛中压入子弹；消费者线程是一个射出线程，它不断从枪膛中射出子弹。
 */
public class GunThreadProblemByReentrantLock {

    private static LinkedList<Integer> queue = new LinkedList<>();
    private static Lock lock = new ReentrantLock();

    static class Shot implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    lock.tryLock(10, TimeUnit.SECONDS);
                    if (queue.size() > 0) {
                        queue.remove();
                        System.out.println(Thread.currentThread().getName() + "-射击：" + queue.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    static class Load implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    lock.tryLock(10, TimeUnit.SECONDS);
                    if (queue.size() < 20) {
                        queue.push(1);
                        System.out.println(Thread.currentThread().getName() + "-装弹：" + queue.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(new Load());
        t1.setName("load");
        t1.start();

        Thread t2 = new Thread(new Shot());
        t2.setName("shotA");
        t2.start();

        Thread t3 = new Thread(new Shot());
        t3.setName("shotB");
        t3.start();
    }
}
