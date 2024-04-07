package com.example.Business.cards.services;

import com.example.Business.cards.models.Consumable;
import com.example.Business.cards.models.Supplier;
import com.example.Business.cards.models.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuppliersService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SuppliersService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Supplier> findAllConsumablesAndSuppliers() {
        List<Supplier> suppliers = this.jdbcTemplate.query(
                "SELECT * FROM Consumable JOIN Supplier ON Consumable.supplier_id = Supplier.id",
                (resultSet, rowNum) -> {
                    Supplier supplier = new Supplier();
                    supplier.setOrganizationName(resultSet.getString("organizationName"));
                    supplier.setId(resultSet.getInt("id"));
                    supplier.setConsumables(findAllConsumablesBySupplierId(supplier.getId()));

                    return supplier;
                });

        return suppliers;
    }

    public List<Consumable> findAllConsumablesBySupplierId(int id) {
        List<Consumable> consumables = this.jdbcTemplate.query(
                "SELECT * FROM Consumable where supplier_id = ?",
                (resultSet, rowNum) -> {
                    Consumable consumable = new Consumable();
                    consumable.setId(resultSet.getInt("id"));
                    consumable.setAmount(resultSet.getInt("amount"));
                    consumable.setType(resultSet.getString("type"));
                    return consumable;
                }, id);
        return consumables;
    }

    public int findIdByOrgName(String orgName){
        Supplier suppliersMain = this.jdbcTemplate.queryForObject(
                "SELECT id FROM Supplier WHERE organizationName = ?",
                (resultSet, rowNum) -> {
                    Supplier supplier = new Supplier();
                    supplier.setId(resultSet.getInt("id"));
                    return supplier;
                }, orgName);

        return suppliersMain.getId();
    }

    public void save(Supplier supplier){
        this.jdbcTemplate.update("INSERT INTO Supplier (organizationName) values (?)", supplier.getOrganizationName());
    }
}
