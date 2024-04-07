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

    //TODO удалять заказы и создавать архив удаленных заказов

    public List<Request> findRequestsByWorkerId(int id) {
        List<Request> requests = this.jdbcTemplate.query(
                "SELECT Request.id FROM Request INNER JOIN Worker ON Request.worker_id=Worker.id where worker_id = ? ",
                (resultSet, rowNum) -> {
                    Request request = new Request();

                    request.setId(resultSet.getInt("Request.id"));
                    return request;
                }, id);
        return requests;
    }

    public List<Request> findRequestsByCustomerId(int id) {
        List<Request> requests = this.jdbcTemplate.query(
                "SELECT * FROM Request INNER JOIN Customer ON Request.customer_id=Customer.id INNER JOIN Worker ON Request.worker_id=Worker.id INNER JOIN Design ON Request.design_id=Design.id WHERE Customer.id = ?",
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
        return requests;
    }

    public void fillConsumablesBasketArchiveByRequestId(int id){
        this.jdbcTemplate.update("INSERT INTO ConsumablesBasketArchive SELECT * FROM ConsumablesBasket WHERE request_id = ?", id);
    }

    public void deleteFromConsumablesBasketByRequestId(int id){
        this.jdbcTemplate.update("DELETE FROM ConsumablesBasket WHERE request_id = ?", id);
    }

    public void saveRequest(String font, String workerName, String customerName, String customerPhoneNumber,Request request, boolean isCustomerExist){
        if (!isCustomerExist){
        this.jdbcTemplate.update("INSERT INTO Customer (fullName,phoneNumber) values (?,?)",customerName,customerPhoneNumber);
        }

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

        List<Consumable> consumablesForPaperAmountAndId = jdbcTemplate.query(
                "select amount,id from Consumable where type = 'Лист А4' and amount >= ? ",
                (resultSet, rowNum) -> {
                    Consumable newConsumable = new Consumable();
                    newConsumable.setAmount(resultSet.getInt("amount"));
                    newConsumable.setId(resultSet.getInt("id"));
                    return newConsumable;
                },paperAmount);

        this.jdbcTemplate.update("INSERT INTO ConsumablesBasket (consumable_id, request_id, amountInRequest)\n" +
                "SELECT\n" +
                "    ?,\n" +
                "    (SELECT id FROM Request WHERE text = ?),\n" +
                "    ?", consumablesForPaperAmountAndId.get(0).getId(),request.getText(),paperAmount);

        this.jdbcTemplate.update("UPDATE Consumable set amount = ? where id = ?",consumablesForPaperAmountAndId.get(0).getAmount()-paperAmount,consumablesForPaperAmountAndId.get(0).getId());

        List<Consumable> consumableForPenAmountAndId = jdbcTemplate.query(
                "select amount,id from Consumable where type = 'Шариковая ручка' and amount >= ?",
                (resultSet, rowNum) -> {
                    Consumable newConsumable = new Consumable();
                    newConsumable.setAmount(resultSet.getInt("amount"));
                    newConsumable.setId(resultSet.getInt("id"));
                    return newConsumable;
                },penAmount);

        this.jdbcTemplate.update("INSERT INTO ConsumablesBasket (consumable_id, request_id, amountInRequest)\n" +
                "SELECT\n" +
                "    ?,\n" +
                "    (SELECT id FROM Request WHERE text = ?),\n" +
                "    ?",consumableForPenAmountAndId.get(0).getId(),request.getText(),penAmount);

        this.jdbcTemplate.update("UPDATE Consumable set amount = ? where id = ?",consumableForPenAmountAndId.get(0).getAmount()-penAmount,consumableForPenAmountAndId.get(0).getId());

    }

    public void endUpRequest(int id){
        this.jdbcTemplate.update("update Request set endDate = ? where id = ?",java.time.LocalDate.now(),id);
    }

    public int findDoneRequestsNumber(Date firstDate, Date secondDate) {
        return this.jdbcTemplate.queryForObject("SELECT SUM(totalCount) FROM (SELECT COUNT(id) as totalCount FROM Request where startDate >= ? and endDate <= ? and endDate is not null UNION ALL SELECT COUNT(id) as totalCount FROM RequestArchive where startDate >= ? and endDate <= ? and endDate is not null) AS subquery", Integer.class,firstDate,secondDate,firstDate,secondDate);
    }

    public int findUsedConsumablesSum(Date firstDate, Date secondDate, String consumableType) {
        return this.jdbcTemplate.queryForObject("SELECT SUM(amountInRequest) FROM Consumable INNER JOIN ConsumablesBasket ON ConsumablesBasket.consumable_id=Consumable.id INNER JOIN Request ON ConsumablesBasket.request_id=Request.id WHERE Request.startDate >= ? AND Request.endDate <= ? AND Request.endDate is not null AND Consumable.type = ?", Integer.class,firstDate,secondDate,consumableType);

    }


    public void createRequestArchive(){
        this.jdbcTemplate.execute("CREATE TABLE RequestArchive SELECT * FROM Request WHERE 1=0");
    }

    public void createConsumablesBasketArchive(){
        this.jdbcTemplate.execute("CREATE TABLE ConsumablesBasketArchive SELECT * FROM ConsumablesBasket WHERE 1=0");
    }


    public void deleteRequest(int id){
        this.jdbcTemplate.update("DELETE FROM Request where id = ?",id);
    }





}
