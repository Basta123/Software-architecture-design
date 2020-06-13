package com.dbtaxi.service.people;

import com.dbtaxi.model.people.Operator;
import com.dbtaxi.model.people.User;
import com.dbtaxi.repository.OperatorRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class OperatorService implements UserService {

    @Autowired
    private OperatorRepository operatorRepository;

    @Override
    public void save(User user) {
        Operator operator = (Operator) user;
        operatorRepository.save(operator);
    }

    @Override
    public User getUserByUsername(String username) {
        Operator operator = operatorRepository.getOperatorByUsername(username);
        return operator;
    }
}
