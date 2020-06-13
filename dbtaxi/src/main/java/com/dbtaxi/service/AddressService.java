package com.dbtaxi.service;

import com.dbtaxi.model.Address;

public interface AddressService {
    Address getAddressByMicrodistrictAndStreet(String microdistrict, String street);

    void saveAddress(Address address);
}
