package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "Request")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Request {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "design_id", referencedColumnName = "id")
    private Design design;

    @ManyToOne
    @JoinColumn(name = "worker_id", referencedColumnName = "id")
    private Worker worker;

    @OneToMany(mappedBy = "request")
    private List<ConsumablesBasket> consumablesBaskets;

    @NotNull(message = "Количетсво визиток не должен быть пустым")
    @Min(value = 1, message = "Минимальное колчество визиток - 1")
    @Max(value = 1000, message = "Максимальное количество визиток - 1000")
    @Column(name = "cardsAmount")
    private int cardsAmount;

    @NotEmpty(message = "not empty")
    @Size(min = 1, max = 40, message = "Количество символов в тексте визитки должно быть от 1 до 40")
    @Column(name = "text")
    private String text;


    @Column(name = "startDate")
    private java.sql.Date startDate;


    @Column(name = "endDate")
    private java.sql.Date endDate;

}
