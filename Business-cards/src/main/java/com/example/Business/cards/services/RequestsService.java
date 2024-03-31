package com.example.Business.cards.services;

import com.example.Business.cards.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class RequestsService {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RequestsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Request> findNotEndedRequestsId() {
        List<Request> requests = this.jdbcTemplate.query(
                "SELECT id FROM Request where endDate is null",
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
                    design.setId(resultSet.getInt("id"));
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
                    worker.setId(resultSet.getInt("id"));
                    worker.setFullName(resultSet.getString("fullName"));
                    worker.setPhoneNumber(resultSet.getString("phoneNumber"));
                    return worker;
                });
        return workers;
    }

    //TODO спросить чо тут делать с айдишников работника

    public void deleteWorkerById(int id){
        this.jdbcTemplate.update("UPDATE Request SET worker_id=null WHERE worker_id = ?",id);
        this.jdbcTemplate.update("DELETE FROM Worker WHERE id = ?", id);
    }




    public List<Request> findAllRequestsIdAndTheirCustomers() {
        List<Request> requests = this.jdbcTemplate.query(
                "SELECT Request.id, fullName, phoneNumber FROM Request INNER JOIN Customer ON Request.customer_id=Customer.id",
                (resultSet, rowNum) -> {
                    Request request = new Request();
                    Customer customer = new Customer();
                    customer.setFullName(resultSet.getString("fullName"));
                    customer.setPhoneNumber(resultSet.getString("phoneNumber"));
                    request.setId(resultSet.getInt("Request.id"));
                    request.setCustomer(customer);
                    return request;
                });
        return requests;
    }


    public void saveRequest(String font, String workerName, String customerName, String customerPhoneNumber,Request request){
        this.jdbcTemplate.update("INSERT INTO Customer (fullName,phoneNumber) values (?,?)",customerName,customerPhoneNumber);
        this.jdbcTemplate.update("INSERT INTO Request (customer_id, design_id, worker_id, cardsAmount, text, startDate, endDate)\n" +
                                        "SELECT\n" +
                                        "    (SELECT id FROM Customer WHERE fullName = ?),\n" +
                                        "    (SELECT id FROM Design WHERE font = ?),\n" +
                                        "    (SELECT id FROM Worker WHERE fullName = ?),\n" +
                                        "    ?,\n" +
                                        "    ?,\n" +
                                        "    ?,\n" +
                                        "    ?",customerName,font,workerName,request.getCardsAmount(),request.getText(),java.time.LocalDate.now(),null);

        int paperAmount = (int) Math.round(request.getCardsAmount() * 0.2);
        int penAmount = (int) Math.round(request.getCardsAmount() * 0.1);

        this.jdbcTemplate.update("INSERT INTO ConsumablesBasket (consumable_id, request_id, amountInRequest)\n" +
                "SELECT\n" +
                "    (SELECT id FROM Consumable WHERE type = 'Лист А4' AND amount >= ?),\n" +
                "    (SELECT id FROM Request WHERE text = ?),\n" +
                "    ?", paperAmount,request.getText(),paperAmount);

        Consumable consumableForPaperAmount = jdbcTemplate.queryForObject(
                "select amount from Consumable where type = 'Лист А4'",
                (resultSet, rowNum) -> {
                    Consumable newConsumable = new Consumable();
                    newConsumable.setAmount(resultSet.getInt("amount"));
                    return newConsumable;
                });

        this.jdbcTemplate.update("UPDATE Consumable set amount = ? where type = 'Лист А4'",consumableForPaperAmount.getAmount()-paperAmount);

        this.jdbcTemplate.update("INSERT INTO ConsumablesBasket (consumable_id, request_id, amountInRequest)\n" +
                "SELECT\n" +
                "    (SELECT id FROM Consumable WHERE type = 'Шариковая ручка' AND amount >= ?),\n" +
                "    (SELECT id FROM Request WHERE text = ?),\n" +
                "    ?", penAmount,request.getText(),penAmount);

        Consumable consumableForPenAmount = jdbcTemplate.queryForObject(
                "select amount from Consumable where type = 'Шариковая ручка'",
                (resultSet, rowNum) -> {
                    Consumable newConsumable = new Consumable();
                    newConsumable.setAmount(resultSet.getInt("amount"));
                    return newConsumable;
                });

        this.jdbcTemplate.update("UPDATE Consumable set amount = ? where type = 'Шариковая ручка'",consumableForPenAmount.getAmount()-penAmount);

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

                    consumable.setAmount(resultSet.getInt("amount"));
                    consumable.setType(resultSet.getString("type"));
                    return consumable;
                }, id);
        return consumables;
    }

    public void endUpRequest(int id){
        this.jdbcTemplate.update("update Request set endDate = ? where id = ?",java.time.LocalDate.now(),id);
    }

    public int findDoneRequestsNumber(Date firstDate, Date secondDate) {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(id)  FROM Request where startDate >= ? and endDate <= ? and endDate is not null", Integer.class,firstDate,secondDate);
    }

    public int findUsedConsumablesSum(Date firstDate, Date secondDate, String consumableType) {
        return this.jdbcTemplate.queryForObject("SELECT SUM(amountInRequest) FROM Consumable INNER JOIN ConsumablesBasket ON ConsumablesBasket.consumable_id=Consumable.id INNER JOIN Request ON ConsumablesBasket.request_id=Request.id WHERE Request.startDate >= ? AND Request.endDate <= ? AND Request.endDate is not null AND Consumable.type = ?", Integer.class,firstDate,secondDate,consumableType);

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
