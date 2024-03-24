package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ConsumablesBasket")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ConsumablesBasket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "consumable_id", referencedColumnName = "id")
    private Consumable consumable;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "id")
    private Request request;

    @NotEmpty(message = "Количество в заказе не должен быть пустым")
    @Min(1)
    @Column(name = "amountInRequest")
    private int amountInRequest;
}
