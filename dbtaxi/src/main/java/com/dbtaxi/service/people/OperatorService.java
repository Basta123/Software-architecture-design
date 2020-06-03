package com.dbtaxi.service.people;

import com.dbtaxi.model.people.Operator;
import com.dbtaxi.repository.OperatorRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    public Operator getOperatorByUsername(String username) {
        Operator operator = operatorRepository.getOperatorByUsername(username);
        return operator;
    }

}
