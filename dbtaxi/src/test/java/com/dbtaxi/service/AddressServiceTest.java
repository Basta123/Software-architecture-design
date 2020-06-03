package com.dbtaxi.service;

import com.dbtaxi.model.Address;
import com.dbtaxi.repository.AddressRepository;
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
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @MockBean
    private AddressRepository addressRepository;

    @Test
    void getAddressByMicrodistrictAndStreet() {
        String microdistrict = "microdistrict";
        String street = "street";
        Address address = new Address();
        address.setMicrodistrict(microdistrict);
        address.setStreet(street);

        when(addressRepository.getAddressByMicrodistrictAndStreet(microdistrict, street)).thenReturn(address);
        assertEquals("microdistrict", addressService.getAddressByMicrodistrictAndStreet(microdistrict, street).getMicrodistrict());
        assertEquals("street", addressService.getAddressByMicrodistrictAndStreet(microdistrict, street).getStreet());

    }

    @Test
    void saveAddress() {
        Address address = new Address();
        addressService.saveAddress(address);
        verify(addressRepository, times(1)).save(address);
    }
}
