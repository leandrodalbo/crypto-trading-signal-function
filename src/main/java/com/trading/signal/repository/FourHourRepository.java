package com.trading.signal.repository;


import com.trading.signal.entity.FourHour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FourHourRepository extends CrudRepository<FourHour, String> {}
