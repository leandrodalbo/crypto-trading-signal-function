package com.trading.signal.functions;


import java.util.function.Function;

public class Refresh implements Function<Object, Object> {
    public static final String REFRESH_COMPLETED = "refresh-function-completed";

    @Override
    public Object apply(Object o) {
        return REFRESH_COMPLETED;
    }
}
