package com.trading.signal.repository;

import com.trading.signal.entity.OneDay;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneDayRepository extends ReactiveCrudRepository<OneDay, String> {}
