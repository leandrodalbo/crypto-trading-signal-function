package com.trading.signal.repository;


import com.trading.signal.entity.OneHour;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneHourRepository extends CrudRepository<OneHour, String> {}
