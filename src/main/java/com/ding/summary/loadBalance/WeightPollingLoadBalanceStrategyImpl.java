package com.ding.summary.loadBalance;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 加权轮询策略
 * 2019/3/11
 */
public class WeightPollingLoadBalanceStrategyImpl implements LoadBalanceStrategy{

    private int index = 0;

    // ip:port:weight
    @Override
    public String select(List<String> ids) {
        List<String> idAndPortList =Lists.newArrayList();
        String target = null;

        for (String id: ids) {
            int lastIndex = id.lastIndexOf(":");
            int weight = Integer.valueOf(id.substring(lastIndex + 1));

            for (int i = 0; i < weight; i++) {
                idAndPortList.add(id.substring(0, lastIndex));
            }
        }

        try {
            synchronized (this) {
                if (index >= idAndPortList.size()) {
                    index = 0;
                }
                target = idAndPortList.get(index++);
            }
        } catch (RuntimeException e) {
            System.out.println("错了================" + e.getMessage());
        }

        if (Objects.isNull(target)) {
            target = ids.get(0).substring(0, ids.get(0).lastIndexOf(":"));
        }

        return target;
    }

    public static void main(String[] args) {
        WeightPollingLoadBalanceStrategyImpl loadBalanceStrategy = new WeightPollingLoadBalanceStrategyImpl();
        List<String> ids = Lists.newArrayList("192.168.39.11:8080:5", "192.168.39.42:7777:10", "192.168.39.79:9999:2");

        ExecutorService service = Executors.newScheduledThreadPool(4);

        for (int i = 0; i < 4; i ++) {
            service.execute(() -> {
                for (int j = 0; j < 10000; j++) {
//                    System.out.println(Thread.currentThread().getName() + "---" + loadBalanceStrategy.select(ids));
                    loadBalanceStrategy.select(ids);
                }
            });
        }

        service.shutdown();
    }
}
