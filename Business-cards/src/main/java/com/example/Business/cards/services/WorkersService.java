package com.example.Business.cards.services;

import com.example.Business.cards.models.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkersService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public WorkersService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Worker> findAllWorkers() {
        List<Worker> workers = this.jdbcTemplate.query(
                "SELECT * FROM Worker",
                (resultSet, rowNum) -> {
                    Worker worker = new Worker();
                    worker.setId(resultSet.getInt("id"));
                    worker.setFullName(resultSet.getString("fullName"));
                    worker.setPhoneNumber(resultSet.getString("phoneNumber"));
                    return worker;
                });
        return workers;
    }

    public List<Worker> findAvailableWorkers() {
        List<Worker> workers = this.jdbcTemplate.query(
                "SELECT Worker.* FROM Worker LEFT JOIN Request ON Worker.id = Request.worker_id AND Request.endDate IS NULL GROUP BY Worker.id HAVING COUNT(Request.worker_id) < 3 OR COUNT(Request.worker_id) = 0;",
                (resultSet, rowNum) -> {
                    Worker worker = new Worker();
                    worker.setId(resultSet.getInt("id"));
                    worker.setFullName(resultSet.getString("fullName"));
                    worker.setPhoneNumber(resultSet.getString("phoneNumber"));
                    return worker;
                });
        return workers;
    }

    public void deleteWorkerById(int id){
        this.jdbcTemplate.update("INSERT INTO RequestArchive SELECT * FROM Request WHERE worker_id = ?", id);
        this.jdbcTemplate.update("DELETE FROM Request WHERE worker_id = ?",id);
        this.jdbcTemplate.update("DELETE FROM Worker WHERE id = ?", id);
    }

    public void save(Worker worker){
        this.jdbcTemplate.update("INSERT INTO Worker (fullName, phoneNumber) values (?,?)",worker.getFullName(),worker.getPhoneNumber());
    }

    public void update(Worker worker, int id){
        this.jdbcTemplate.update("UPDATE Worker SET fullName = ?, phoneNumber = ? where id = ?",worker.getFullName(),worker.getPhoneNumber(),id);
    }
}
