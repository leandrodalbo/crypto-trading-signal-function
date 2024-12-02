package com.trading.signal.repository;


import com.trading.signal.entity.OneHour;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneHourRepository extends ReactiveCrudRepository<OneHour, String> {}
