package com.dbtaxi.service;

import com.dbtaxi.model.Bankcard;
import com.dbtaxi.repository.BankcardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class BankcardServiceTest {

    @Autowired
    private BankcardService bankcardService;

    @MockBean
    private BankcardRepository bankcardRepository;

    @Test
    void increment() {
        Bankcard bankcard = new Bankcard();
        bankcard.setBalance(2000);

        bankcardService.increment(bankcard, 500);
        verify(bankcardRepository, times(1)).save(bankcard);
        assertEquals(2500, bankcard.getBalance());
    }

    @Test
    void decrement() {
        Bankcard bankcard = new Bankcard();
        bankcard.setBalance(2000);

        bankcardService.decrement(bankcard, 500);
        verify(bankcardRepository, times(1)).save(bankcard);
        assertEquals(1500, bankcard.getBalance());
    }

    @Test
    void saveBankcard() {
        Bankcard bankcard = new Bankcard();
        bankcardService.saveBankcard(bankcard);
        verify(bankcardRepository, times(1)).save(bankcard);
    }
}
