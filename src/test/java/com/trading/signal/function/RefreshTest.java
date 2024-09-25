package com.trading.signal.function;

import com.trading.signal.functions.Refresh;
import com.trading.signal.service.RefreshService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTest {

    @Mock
    RefreshService service;

    @InjectMocks
    Refresh refreshFunction;

    @Test
    public void shouldCallRefreshService() {
        doNothing().when(service).refresh();

        refreshFunction.accept(new Object());

        verify(service, times(1)).refresh();
    }
}
