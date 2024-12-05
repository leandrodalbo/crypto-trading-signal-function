package com.trading.signal.service;

import com.trading.signal.entity.FourHour;
import com.trading.signal.entity.OneDay;
import com.trading.signal.entity.OneHour;
import com.trading.signal.model.Signal;
import com.trading.signal.repository.FourHourRepository;
import com.trading.signal.repository.OneDayRepository;
import com.trading.signal.repository.OneHourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CommitSignalService {

    private final Logger logger = LoggerFactory.getLogger(CommitSignalService.class);
    private final FourHourRepository fourHourRepository;
    private final OneHourRepository oneHourRepository;
    private final OneDayRepository oneDayRepository;

    public CommitSignalService(FourHourRepository fourHourRepository, OneHourRepository oneHourRepository, OneDayRepository oneDayRepository) {
        this.fourHourRepository = fourHourRepository;
        this.oneHourRepository = oneHourRepository;
        this.oneDayRepository = oneDayRepository;
    }

    public void saveSignal(Signal signal) {
        switch (signal.timeframe()) {
            case H1 -> saveOneHour(signal);
            case H4 -> saveFourHour(signal);
            default -> saveOneDay(signal);
        }
    }

    private void saveOneDay(Signal signal) {

        Optional<OneDay> it = oneDayRepository.findById(signal.symbol());
        if (it.isEmpty()) {
            oneDayRepository.save(OneDay.fromSignal(signal, null));
        } else {
            oneDayRepository.save(OneDay.fromSignal(signal, it.get().version()));
        }
    }

    private void saveOneHour(Signal signal) {
        Optional<OneHour> it = oneHourRepository.findById(signal.symbol());
        if (it.isEmpty()) {
            oneHourRepository.save(OneHour.fromSignal(signal, null));
        } else {
            oneHourRepository.save(OneHour.fromSignal(signal, it.get().version()));
        }
    }

    private void saveFourHour(Signal signal) {
        Optional<FourHour> it = fourHourRepository.findById(signal.symbol());
        if (it.isEmpty()) {
            fourHourRepository.save(FourHour.fromSignal(signal, null));
        } else {
            fourHourRepository.save(FourHour.fromSignal(signal, it.get().version()));
        }
    }
}
