package com.trading.signal.functions;


import com.trading.signal.service.RefreshService;

import java.util.function.Consumer;

public class Refresh implements Consumer<Object> {
    private final RefreshService refreshService;

    public Refresh(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Override
    public void accept(Object o) {
        refreshService.refresh();
    }
}
