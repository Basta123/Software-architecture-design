package com.dbtaxi.service.people;

import com.dbtaxi.model.people.Operator;
import com.dbtaxi.repository.OperatorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OperatorServiceTest {

    @Autowired
    private OperatorService operatorService;

    @MockBean
    private OperatorRepository operatorRepository;

    @Test
    void getOperatorByUsername() {
        Operator operator = new Operator();
        operator.setUsername("o1");
        when(operatorRepository.getOperatorByUsername("o1")).thenReturn(operator);
        assertEquals("o1", operatorService.getOperatorByUsername("o1").getUsername());
    }
}
