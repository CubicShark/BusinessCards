package com.example.Business.cards.services;

import com.example.Business.cards.models.Customer;
import com.example.Business.cards.models.Design;
import com.example.Business.cards.models.Request;
import com.example.Business.cards.models.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class RequestsService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RequestsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Request> findRequestsId() {
        List<Request> requests = this.jdbcTemplate.query(
                "SELECT Request.id FROM Request",
                (resultSet, rowNum) -> {
                    Request request = new Request();
                    request.setId(resultSet.getInt("Request.id"));
                    return request;
                });
        return requests;
    }

    public Request findAllByRequestId(int id) {
            Request requestMain = this.jdbcTemplate.queryForObject(
                "SELECT * FROM Request INNER JOIN Customer ON Request.customer_id=Customer.id INNER JOIN Worker ON Request.worker_id=Worker.id INNER JOIN Design ON Request.design_id=Design.id WHERE Request.id = ?",
                (resultSet, rowNum) -> {
                    Request request = new Request();
                    Customer customer = new Customer();
                    Worker worker = new Worker();
                    Design design = new Design();

                    request.setId(resultSet.getInt("Request.id"));
                    request.setCardsAmount(resultSet.getInt("Request.cardsAmount"));
                    request.setText(resultSet.getString("Request.text"));
                    request.setStartDate(resultSet.getDate("Request.startDate"));
                    request.setEndDate(resultSet.getDate("Request.endDate"));

                    customer.setFullName(resultSet.getString("Customer.fullName"));
                    customer.setPhoneNumber(resultSet.getString("Customer.phoneNumber"));
                    request.setCustomer(customer);

                    worker.setFullName(resultSet.getString("Worker.fullName"));
                    worker.setPhoneNumber(resultSet.getString("Worker.phoneNumber"));
                    request.setWorker(worker);

                    design.setId(resultSet.getInt("Design.id"));
                    design.setFont(resultSet.getString("Design.font"));
                    design.setLetterHeight(resultSet.getInt("Design.letterHeight"));
                    request.setDesign(design);

                    return request;
                }, id);
        return requestMain;
    }

    public List<Design> findAllDesigns() {
        List<Design> designs = this.jdbcTemplate.query(
                "SELECT * FROM Design",
                (resultSet, rowNum) -> {
                    Design design = new Design();
                    design.setFont(resultSet.getString("font"));
                    design.setLetterHeight(resultSet.getInt("letterHeight"));
                    return design;
                });
        return designs;
    }

    public List<Worker> findAllWorkers() {
        List<Worker> workers = this.jdbcTemplate.query(
                "SELECT * FROM Worker",
                (resultSet, rowNum) -> {
                    Worker worker = new Worker();
                    worker.setFullName(resultSet.getString("fullName"));
                    worker.setPhoneNumber(resultSet.getString("phoneNumber"));
                    return worker;
                });
        return workers;
    }


    public void save(String font, String workerName, String customerName, String customerPhoneNumber,Request request){
        this.jdbcTemplate.update("INSERT INTO Customer (fullName,phoneNumber) values (?,?)",customerName,customerPhoneNumber);
        this.jdbcTemplate.update("INSERT INTO Request (customer_id, design_id, worker_id, cardsAmount, text, startDate, endDate)\n" +
                                        "SELECT\n" +
                                        "    (SELECT id FROM Customer WHERE fullName = ?),\n" +
                                        "    (SELECT id FROM Design WHERE font = ?),\n" +
                                        "    (SELECT id FROM Worker WHERE fullName = ?),\n" +
                                        "    ?,\n" +
                                        "    ?,\n" +
                                        "    ?,\n" +
                                        "    ?",customerName,font,workerName,request.getCardsAmount(),request.getText(),request.getStartDate(),request.getEndDate());
    }

}
