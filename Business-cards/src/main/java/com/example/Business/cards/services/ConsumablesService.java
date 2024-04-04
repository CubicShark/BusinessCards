package com.example.Business.cards.services;

import com.example.Business.cards.models.Consumable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumablesService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConsumablesService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Consumable findAvailablePaperNumber(){
        Consumable AvailablePaperAmount = jdbcTemplate.queryForObject(
                "select amount from Consumable where type = 'Лист А4'",
                (resultSet, rowNum) -> {
                    Consumable newConsumable = new Consumable();
                    newConsumable.setAmount(resultSet.getInt("amount"));
                    return newConsumable;
                });
        return AvailablePaperAmount;
    }

    public Consumable findAvailablePenNumber(){
        Consumable AvailablePenAmount = jdbcTemplate.queryForObject(
                "select amount from Consumable where type = 'Шариковая ручка'",
                (resultSet, rowNum) -> {
                    Consumable newConsumable = new Consumable();
                    newConsumable.setAmount(resultSet.getInt("amount"));
                    return newConsumable;
                });
        return AvailablePenAmount;
    }

    public List<String> findConsumableTypes() {
        List<String> types = this.jdbcTemplate.query(
                "SELECT DISTINCT type FROM Consumable",
                (resultSet, rowNum) -> {
                    String type;
                    type = (resultSet.getString("type"));
                    return type;
                });
        return types;
    }
}
