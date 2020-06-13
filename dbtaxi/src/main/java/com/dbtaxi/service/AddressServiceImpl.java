package com.dbtaxi.service;

import com.dbtaxi.model.Address;
import com.dbtaxi.repository.AddressRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address getAddressByMicrodistrictAndStreet(String microdistrict, String street) {
        Address address = addressRepository.getAddressByMicrodistrictAndStreet(microdistrict, street);
        if (address != null) {
            return address;
        }

        saveAddress(new Address(microdistrict, street));
        return getAddressByMicrodistrictAndStreet(microdistrict, street);
    }

    @Override
    public void saveAddress(Address address) {
        addressRepository.save(address);
    }

}
