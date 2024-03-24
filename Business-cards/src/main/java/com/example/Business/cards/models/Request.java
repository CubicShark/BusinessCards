package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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

    @NotEmpty(message = "Количетсво визиток не должен быть пустым")
    @Min(1)
    @Max(1000)
    @Column(name = "cardsAmount")
    private int cardsAmount;

    @NotEmpty(message = "Текст не должно быть пустым")
    @Size(min = 1, max = 40, message = "Количество символов в тексте визитки должно быть от 1 до 40")
    @Column(name = "text")
    private String text;

    @NotEmpty(message = "Дата начала не должна быть пустой")
    @Column(name = "startDate")
    private java.sql.Date startDate;

    @Column(name = "endDate")
    private java.sql.Date endDate;

}
