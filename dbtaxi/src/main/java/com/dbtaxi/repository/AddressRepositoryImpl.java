package com.dbtaxi.repository;

import com.dbtaxi.model.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AddressRepositoryImpl implements AddressRepository {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    @Override
    public Address getAddressByMicrodistrictAndStreet(String microdistrict, String street) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM address WHERE microdistrict=(?) and street=(?)")) {
            statement.setString(1, microdistrict);
            statement.setString(2, street);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Address address = new Address();
                address.setId(resultSet.getInt("id"));
                address.setMicrodistrict(resultSet.getString("microdistrict"));
                address.setStreet(resultSet.getString("street"));
                return address;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Address readById(Integer id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM address WHERE id=(?)")) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Address address = new Address();
                String microdistrict = resultSet.getString("microdistrict");
                String street = resultSet.getString("street");
                address.setId(id);
                address.setMicrodistrict(microdistrict);
                address.setStreet(street);
                return address;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save(Address address) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO address (microdistrict, street) VALUES ( (?),(?))")) {
            statement.setString(1, address.getMicrodistrict());
            statement.setString(2, address.getStreet());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Address> getAddresses() {
        List<Address> addresses = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM address")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Address address = new Address();
                address.setId(resultSet.getInt("id"));
                address.setMicrodistrict(resultSet.getString("microdistrict"));
                address.setStreet(resultSet.getString("street"));
                addresses.add(address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addresses;
    }
}
