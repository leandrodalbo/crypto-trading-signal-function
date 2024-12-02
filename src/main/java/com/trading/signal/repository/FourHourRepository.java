package com.trading.signal.repository;


import com.trading.signal.entity.FourHour;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FourHourRepository extends ReactiveCrudRepository<FourHour, String> {}
