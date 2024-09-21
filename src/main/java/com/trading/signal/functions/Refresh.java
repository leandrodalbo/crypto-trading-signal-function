package com.trading.signal.functions;


import com.trading.signal.service.RefreshService;

import java.util.function.Function;

public class Refresh implements Function<Object, Object> {
    public static final String REFRESH_TRIGGERED = "refresh-function-triggered";

    private final RefreshService refreshService;

    public Refresh(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Override
    public Object apply(Object o) {
        refreshService.refresh();
        return REFRESH_TRIGGERED;
    }
}
