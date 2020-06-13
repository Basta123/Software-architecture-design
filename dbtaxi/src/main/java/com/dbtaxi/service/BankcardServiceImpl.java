package com.dbtaxi.service;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.repository.BankcardRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class BankcardServiceImpl implements BankcardService {

    @Autowired
    private BankcardRepository bankcardRepository;

    @Override
    public void increment(Bankcard bankcard, int fare) {
        int balance = bankcard.getBalance();
        balance += fare;
        bankcard.setBalance(balance);
        saveBankcard(bankcard);
    }

    @Override
    public void decrement(Bankcard bankcard, int fare) {
        int balance = bankcard.getBalance();
        balance -= fare;
        bankcard.setBalance(balance);
        saveBankcard(bankcard);
    }

    @Override
    public void saveBankcard(Bankcard bankcard) {
        bankcardRepository.save(bankcard);
    }
}
