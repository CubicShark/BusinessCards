package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table (name = "Supplier")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Supplier {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Имя организации не должено быть пустым")
    @Column(name = "organizationName")
    private String organizationName;

    @OneToMany(mappedBy = "supplier")
    private List<Consumable> consumables;

    public Supplier(String organizationName) {
        this.organizationName = organizationName;
    }
}
