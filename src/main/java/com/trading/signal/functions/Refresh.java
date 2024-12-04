package com.trading.signal.functions;

import com.trading.signal.service.RefreshService;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class    Refresh implements Function<Object, String> {

    private final RefreshService refreshService;

    public Refresh(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @Override
    public String apply(Object o) {
        refreshService.refresh();
        return "Completed!!!";
    }
}
