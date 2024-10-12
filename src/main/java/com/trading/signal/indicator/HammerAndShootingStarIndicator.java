package com.trading.signal.indicator;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HammerAndShootingStarIndicator {

    public static final String HAMMERS = "H";
    public static final String SHOOTING = "S";

    private final Core core;
    private final ZeroCleaner zeroCleaner;

    public HammerAndShootingStarIndicator(Core core, ZeroCleaner zeroCleaner) {
        this.core = core;
        this.zeroCleaner = zeroCleaner;
    }

    public Map<String, int[]> hammerAndShooting(float[] open, float[] high, float[] low, float[] close) {
        int[] shooting = new int[open.length];
        int[] hammers = new int[open.length];

        core.cdlShootingStar(0, open.length - 1, open, high, low, close, new MInteger(), new MInteger(), shooting);
        core.cdlHammer(0, open.length - 1, open, high, low, close, new MInteger(), new MInteger(), hammers);

        shooting = zeroCleaner.cleanUp(shooting);
        hammers = zeroCleaner.cleanUp(hammers);

        return Map.of(HAMMERS, hammers, SHOOTING, shooting);
    }
}
