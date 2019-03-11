package com.ding.summary.loadBalance;

import com.google.common.collect.Lists;
import org.junit.Test;
import sun.util.resources.cldr.ka.LocaleNames_ka;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 轮询负载均衡策略
 * @date 2019/3/11
 */
public class PollingLoadBalanceStrategyImpl implements LoadBalanceStrategy{

    private int index = 0;
    private Lock lock = new ReentrantLock();

    // ip:port
    @Override
    public String select(List<String> ids) {
        String target = null;

        try {
            lock.tryLock(10, TimeUnit.SECONDS);
            if (index >= ids.size()) {
                index = 0;
            }
            target = ids.get(index++);
        } catch (InterruptedException e) {
            System.out.println("错了-----------------------------------");
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        if (Objects.isNull(target)) {
            return ids.get(0);
        }
        return target;
    }

    public void selectThreadTest() {
        ArrayList<String> ids = Lists.newArrayList("192.168.39.11:8080", "192.168.39.22:7979", "192.168.39.33:3333");
        PollingLoadBalanceStrategyImpl loadBalanceStrategy = new PollingLoadBalanceStrategyImpl();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "--" + loadBalanceStrategy.select(ids));
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "--" + loadBalanceStrategy.select(ids));
            }
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "--" + loadBalanceStrategy.select(ids));
            }
        }).start();
    }

    public void selectExecutorsTest() {
        ArrayList<String> ids = Lists.newArrayList("192.168.39.11:8080", "192.168.39.22:7979", "192.168.39.33:3333");
        PollingLoadBalanceStrategyImpl loadBalanceStrategy = new PollingLoadBalanceStrategyImpl();

        ExecutorService service = Executors.newScheduledThreadPool(4);

        for (int i = 0; i < 4; i ++) {
            service.execute(() -> {
              for (int j = 0; j < 10000; j++) {
                  System.out.println(loadBalanceStrategy.select(ids));
//                  loadBalanceStrategy.select(ids);
              }
            });

        }
        service.shutdown();
    }

    public static void main(String[] args) {
//        new PollingLoadBalanceStrategyImpl().selectThreadTest();
        new PollingLoadBalanceStrategyImpl().selectExecutorsTest();
    }
}
