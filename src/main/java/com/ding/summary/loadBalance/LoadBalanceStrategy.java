package com.ding.summary.loadBalance;

import java.util.List;

public interface LoadBalanceStrategy {

    public String select(List<String> ids);
}
