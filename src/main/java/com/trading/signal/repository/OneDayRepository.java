package com.trading.signal.repository;

import com.trading.signal.entity.OneDay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneDayRepository extends CrudRepository<OneDay, String> {}
