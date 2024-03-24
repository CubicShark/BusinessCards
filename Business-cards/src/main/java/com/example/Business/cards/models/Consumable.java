package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "Consumable")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Consumable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Количество не должно быть пустым")
    @Min(value = 1,message = "Количество не может быть меньше 1")
    @Column(name = "amount")
    private int amount;

    @NotEmpty(message = "Тип не должно быть пустым")
    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private Supplier supplier;

    @OneToMany(mappedBy = "consumable")
    private List<ConsumablesBasket> consumablesBaskets;
}
