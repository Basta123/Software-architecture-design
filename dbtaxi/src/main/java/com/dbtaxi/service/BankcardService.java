package com.dbtaxi.service;

import com.dbtaxi.model.Bankcard;

public interface BankcardService {
    void increment(Bankcard bankcard, int fare);

    void decrement(Bankcard bankcard, int fare);

    void saveBankcard(Bankcard bankcard);
}
