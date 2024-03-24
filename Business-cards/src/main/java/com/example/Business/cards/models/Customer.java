package com.example.Business.cards.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table (name = "Customer")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Size(min = 2, max = 40, message = "Количество символов в фио должно быть от 2 до 40")
    @Column(name = "fullName")
    private String fullName;


    @Pattern(regexp="^\\+[0-9]{12}$", message="Некорректный формат номера телефона")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer")
    private List<Request> requests;

    public Customer(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }
}
