package com.dbtaxi.repository;

import com.dbtaxi.model.Address;

import java.util.List;

public interface AddressRepository {
    Address getAddressByMicrodistrictAndStreet(String microdistrict, String street);
    Address readById(Integer id);
    void save(Address address);
    List<Address> getAddresses();
}
