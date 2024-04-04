package com.example.Business.cards.services;

import com.example.Business.cards.models.Design;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesignsService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DesignsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Design> findAllDesigns() {
        List<Design> designs = this.jdbcTemplate.query(
                "SELECT * FROM Design",
                (resultSet, rowNum) -> {
                    Design design = new Design();
                    design.setId(resultSet.getInt("id"));
                    design.setFont(resultSet.getString("font"));
                    design.setLetterHeight(resultSet.getInt("letterHeight"));
                    return design;
                });
        return designs;
    }
}
