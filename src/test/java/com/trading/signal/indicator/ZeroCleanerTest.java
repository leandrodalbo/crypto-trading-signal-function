package com.trading.signal.indicator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ZeroCleanerTest {

    ZeroCleaner cleaner = new ZeroCleaner();

    @Test
    void shouldRemoveZerosForArrayOfDouble() {
        assertThat(cleaner.cleanUp(new double[]{0.0})).isEqualTo(new double[0]);
        assertThat(cleaner.cleanUp(new double[]{1.0f, 0.0f})).isEqualTo(new double[]{1.0f});
        assertThat(cleaner.cleanUp(new double[]{1.0f, 2.0f, 0.0f, 0.0f})).isEqualTo(new double[]{1.0f, 2.0f});
    }

    @Test
    void shouldRemoveZerosForArraysOfInteger() {
        assertThat(cleaner.cleanUp(new int[]{0})).isEqualTo(new int[0]);
        assertThat(cleaner.cleanUp(new int[]{1, 0})).isEqualTo(new int[]{1});
        assertThat(cleaner.cleanUp(new int[]{1, 2, 0, 0})).isEqualTo(new int[]{1, 2});
    }
}
