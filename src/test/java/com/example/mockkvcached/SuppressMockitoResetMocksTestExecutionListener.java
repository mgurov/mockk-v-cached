package com.example.mockkvcached;

import org.springframework.boot.test.context.DefaultTestExecutionListenersPostProcessor;
import org.springframework.test.context.TestExecutionListener;

import java.util.Set;
import java.util.stream.Collectors;

public class SuppressMockitoResetMocksTestExecutionListener implements DefaultTestExecutionListenersPostProcessor {
    @Override
    public Set<Class<? extends TestExecutionListener>> postProcessDefaultTestExecutionListeners(Set<Class<? extends TestExecutionListener>> listeners) {
        return listeners.stream()
                .filter((listener) -> listener != org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener.class)
                .collect(Collectors.toSet());
    }
}
