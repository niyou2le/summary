package com.ding.summary.multithread;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 生产者消费者问题
 * 采用Java 多线程技术（例如wait和notify），设计实现一个符合生产者和消费者问题的程序。对一个对象（枪膛）进行操作，
 * 其最大容量是20颗子弹。生产者线程是一个压入线程，它不断向枪膛中压入子弹；消费者线程是一个射出线程，它不断从枪膛中射出子弹。
 */
public class GunThreadProblemByWaitNotify {

    private static Queue<Integer> queue = new LinkedList<>();
    private static final String LOCK = "lock";

    static class Shot extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized(LOCK) {
                    if (queue.size() > 0) {
                        queue.poll();
                        System.out.println("射击---" + queue.size());
                        LOCK.notifyAll();
                    } else {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    static class Load extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    if (queue.size() < 20) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        queue.offer(1);
                        System.out.println("装弹+++" + queue.size());
                        LOCK.notifyAll();
                    } else {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Load().start();
        new Shot().start();
        new Shot().start();
        new Shot().start();
        new Shot().start();
    }

}
