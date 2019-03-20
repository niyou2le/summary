package com.ding.summary.multithread;

import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 生产者消费者问题
 * 采用Java 多线程技术（例如wait和notify），设计实现一个符合生产者和消费者问题的程序。对一个对象（枪膛）进行操作，
 * 其最大容量是20颗子弹。生产者线程是一个压入线程，它不断向枪膛中压入子弹；消费者线程是一个射出线程，它不断从枪膛中射出子弹。
 */
public class GunThreadProblemByBlockingQueue {

    private static BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(20);

    static class Shot extends Thread {

        private String name;

        public Shot(String name) {
            super.setName(name);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    queue.take();
                    System.out.println("射击----Thread:" + Thread.currentThread().getName() + ",size:" + queue.size());
                } catch (InterruptedException e) {
                    try {
                        System.out.println("===================================================");
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    static class Load extends Thread {

        private String name;

        public Load(String name) {
            super.setName(name);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    queue.put(1);
                    System.out.println("上膛+++Thread:" + Thread.currentThread().getName() + ",size:" + queue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Load("load1").start();
        new Load("load2").start();
        new Load("load3").start();
        new Load("load4").start();
        new Load("load5").start();
        new Shot("shot1").start();
        new Shot("shot2").start();
        new Shot("shot3").start();
        new Shot("shot4").start();
        new Shot("shot5").start();
        new Shot("shot6").start();
    }
}
