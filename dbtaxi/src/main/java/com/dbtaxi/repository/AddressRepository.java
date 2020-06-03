package com.dbtaxi.repository;

import com.dbtaxi.model.Address;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class AddressRepository {

    private String url = "jdbc:mysql://localhost:3306/dbtaxi?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private String username = "root";
    private String password = "root";

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
}
