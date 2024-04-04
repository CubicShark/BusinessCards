package com.example.Business.cards.services;

import com.example.Business.cards.models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomersService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CustomersService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> findAllCustomers() {
        List<Customer> customers = this.jdbcTemplate.query(
                "SELECT * FROM Customer ",
                (resultSet, rowNum) -> {
                    Customer customer = new Customer();
                    customer.setFullName(resultSet.getString("fullName"));
                    customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                    customer.setId(resultSet.getInt("id"));
                    return customer;
                });
        return customers;
    }

    public Customer findCustomerByFullName(String fullName) {
        Customer customerForPhoneNumber = this.jdbcTemplate.queryForObject(
                "SELECT phoneNumber FROM Customer where fullName = ?",
                (resultSet, rowNum) -> {
                    Customer customer = new Customer();
                    customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                    return customer;
                },fullName);
        return customerForPhoneNumber;
    }
}
